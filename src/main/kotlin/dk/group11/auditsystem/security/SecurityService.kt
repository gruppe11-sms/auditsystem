package dk.group11.auditsystem.security

import dk.group11.auditsystem.clients.RoleClient
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.ResponseStatus
import javax.servlet.http.HttpServletRequest

@ResponseStatus(value = HttpStatus.FORBIDDEN)
class ForbiddenException(message: String) : RuntimeException(message)

@Service
class SecurityService(
        private val request: HttpServletRequest,
        private val roleClient: RoleClient
) : ISecurityService {


    override fun getId(): Long {
        return principal.id
    }

    override fun getToken(): String {
        return this.request.getHeader(HEADER_STRING)
    }

    val principal: UserData
        get() = SecurityContextHolder.getContext().authentication.principal as UserData


    /**
     * Requires the user has the given roles
     * If some of the roles are missing, this method will throws Forbidden
     */
    fun requireRoles(vararg roleKeys: String) {
        val hasRoles = roleClient.hasRoles(getToken(), *roleKeys)
        if (!hasRoles) {
            throw ForbiddenException("Required roles: ${roleKeys.joinToString(", ")}")
        }
    }
}