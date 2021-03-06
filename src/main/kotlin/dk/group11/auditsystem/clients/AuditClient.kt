package dk.group11.auditsystem.clients

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.kittinunf.fuel.Fuel
import dk.group11.auditsystem.security.HEADER_STRING
import dk.group11.auditsystem.security.SecurityService
import org.springframework.beans.BeanUtils
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service

interface IAuditClient {
    fun createEntry(action: String, data: Any)
}

@Service
class AuditClient(private val auditConfigProperties: AuditConfigProperties, private val securityService: SecurityService) : IAuditClient {

    override fun createEntry(action: String, data: Any) {
        val json = getJson(action, data)
        val (request, response, result) = Fuel.post(auditConfigProperties.url + "/api/auditentry")
                .header(Pair(HEADER_STRING, securityService.getToken()))
                .header(Pair("Content-Type", "application/json"))
                .body(json)
                .responseString()
    }

    private fun getJson(action: String, data: Any): String {

        val requestData = if (BeanUtils.isSimpleValueType(data::class.java)) data.toString() else {
            ObjectMapper().writeValueAsString(data)
        }
        val request = AuditRequest(action, requestData)

        return ObjectMapper().writeValueAsString(request)
    }
}

data class AuditRequest(val action: String, val data: Any)

@Configuration
@ConfigurationProperties(prefix = "audit")
data class AuditConfigProperties(var url: String = "")