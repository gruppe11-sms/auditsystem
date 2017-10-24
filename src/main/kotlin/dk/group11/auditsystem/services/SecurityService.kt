package dk.group11.auditsystem.services

import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class SecurityService : ISecurityService {

    override fun getId(): Long {
        return 4
    }
}