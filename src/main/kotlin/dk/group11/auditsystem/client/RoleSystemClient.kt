package dk.group11.auditsystem.client

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.ResponseDeserializable
import dk.group11.auditsystem.config.ConfigProperties
import dk.group11.auditsystem.models.User
import dk.group11.auditsystem.security.HEADER_STRING
import org.springframework.stereotype.Service
import java.util.*

private object UserType : TypeReference<Map<String, String>>()
private class UserDeserializer : ResponseDeserializable<Map<String, String>> {
    override fun deserialize(content: String) = ObjectMapper().readValue<Map<String, String>>(content, UserType)
}


@Service
class RoleSystemClient(val config: ConfigProperties) {

    fun getUsers(userIds: List<Long>, authToken : String): List<User> {
        val ids = userIds.joinToString(separator = ",")

        val (request, response, result) = Fuel.get("${config.roleSystemUrl}/api/users/names", listOf(Pair("userIds", ids)))
                .header(Pair(HEADER_STRING, authToken))
                .responseObject(UserDeserializer())

        val (userMap, error) = result
        if(error != null) {
            throw error.exception
        }

        if(userMap != null) {
            return userMap.map { User(id = it.key.toLong(), name = it.value) }
        }

        return userIds.map { User(name = "", id = it) }

    }
}