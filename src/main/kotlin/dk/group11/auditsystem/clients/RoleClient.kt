package dk.group11.auditsystem.clients

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Deserializable
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.core.response
import com.github.kittinunf.result.Result
import dk.group11.auditsystem.models.User
import dk.group11.auditsystem.security.HEADER_STRING
import dk.group11.auditsystem.security.TOKEN_PREFIX
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
class InternalServerError(message: String) : RuntimeException(message)

class AccessDenied : RuntimeException()
class RequestFailed : RuntimeException()

private val JsonContentType = Pair("Content-Type", "application/json")

@Service
class RoleClient(private val roleConfigProperties: RoleConfigProperties) : IRoleSystemClient {


    private object UserType : TypeReference<Map<String, String>>()
    private class UserDeserializer : ResponseDeserializable<Map<String, String>> {
        override fun deserialize(content: String) = ObjectMapper().readValue<Map<String, String>>(content, UserType)
    }


    private interface Jsonable {
        fun toJSON(): String = ObjectMapper().writeValueAsString(this)
    }

    private data class assertRoleResponse(var success: Boolean)

    private object assetRoleResponseDeserializer : Deserializable<assertRoleResponse> {
        override fun deserialize(response: Response): assertRoleResponse {
            return ObjectMapper().readValue<assertRoleResponse>(response.dataStream, assertRoleResponse::class.java)
        }

    }

    private data class createRoleRequest(val key: String, val title: String, val description: String) : Jsonable

    private var token: String = ""

    private class loginRequest(val username: String, val password: String) : Jsonable

    @Retryable(backoff = Backoff(delay = 3000))
    private fun login() {
        val requestJson = loginRequest(roleConfigProperties.username, roleConfigProperties.password).toJSON()
        val (_, response, result) = Fuel.post("${roleConfigProperties.url}/login")
                .header(JsonContentType)
                .body(requestJson)
                .responseString()

        when (result) {
            is Result.Success -> {
                if (response.statusCode == HttpStatus.OK.value()) {
                    val authHeaders = response.headers[HEADER_STRING]
                    if (authHeaders != null) {
                        token = authHeaders.first()
                        println("Set token: " + token)
                        return
                    }
                }
            }
            else -> {
            }
        }

        throw InternalServerError("Could not authorize with role system")
    }


    /**
     * Ensures that a given role exists
     */
    @Retryable(maxAttempts = 3, backoff = Backoff(delay = 3000), value = *arrayOf(AccessDenied::class, RequestFailed::class))
    fun ensureRole(key: String, title: String, description: String) {
        val (_, response, result) = Fuel.post("${roleConfigProperties.url}/api/roles")
                .header(JsonContentType)
                .header(Pair(HEADER_STRING, "$TOKEN_PREFIX $token"))
                .body(createRoleRequest(key, title, description).toJSON())
                .responseString()

        println("Request")
        when (result) {
            is Result.Failure -> {

                login()
                throw RequestFailed()
            }
            else -> {
                if (response.statusCode == HttpStatus.FORBIDDEN.value()) {
                    login()
                    throw AccessDenied()
                }
            }
        }

    }

    /**
     * Verifies that a user is assigned a given role
     */
    @Retryable(value = InternalServerError::class, backoff = Backoff(delay = 3000))
    fun hasRoles(authHeader: String, vararg roleKeys: String): Boolean {
        val (_, _, result) = Fuel.get("${roleConfigProperties.url}/api/user/verify", listOf(Pair("roles", roleKeys.joinToString(","))))
                .header(Pair(HEADER_STRING, authHeader))
                .response(assetRoleResponseDeserializer)

        when (result) {
            is Result.Failure -> {
                throw InternalServerError("Could not contact role system")
            }
            is Result.Success -> {
                return result.value.success
            }
        }
    }

    override fun getUsers(userIds: List<Long>, authToken: String): List<User> {
        val ids = userIds.joinToString(separator = ",")

        val (request, response, result) = Fuel.get("${roleConfigProperties.url}/api/users/names", listOf(Pair("userIds", ids)))
                .header(Pair(HEADER_STRING, authToken))
                .responseObject(UserDeserializer())

        val (userMap, error) = result
        if (error != null) {
            throw error.exception
        }

        if (userMap != null) {
            return userMap.map { User(id = it.key.toLong(), name = it.value) }
        }

        return userIds.map { User(name = "", id = it) }

    }
}


@Configuration
@ConfigurationProperties(prefix = "role")
data class RoleConfigProperties(
        var url: String = "http://localhost:8084",
        var username: String = "",
        var password: String = ""
)