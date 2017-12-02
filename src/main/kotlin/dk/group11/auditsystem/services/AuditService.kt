package dk.group11.auditsystem.services

import dk.group11.auditsystem.clients.IAuditClient
import dk.group11.auditsystem.clients.IRoleSystemClient
import dk.group11.auditsystem.models.AuditEntry
import dk.group11.auditsystem.models.AuditEntryWithName
import dk.group11.auditsystem.models.Filters
import dk.group11.auditsystem.repositories.AuditRepository
import dk.group11.auditsystem.security.SecurityService
import org.springframework.stereotype.Service

class getAllEntriesAuditData(val action: String, val userId: Long)

@Service
class AuditService(
        private val auditRepository: AuditRepository,
        private val rolesystem: IRoleSystemClient,
        private val auditClient: IAuditClient,
        private val securityService: SecurityService
) : IAuditService {
    override fun createEntry(entry: AuditEntry): AuditEntry {
        return auditRepository.save(entry)
    }

    override fun getAllEntries(type: String, userId: Long): List<AuditEntryWithName> {
        // Create audit entry for tracking any requests
        auditClient.createEntry("[AuditSystem] Get Entries", getAllEntriesAuditData(type, userId))

        // Actually get the entries we care about
        val entries = auditRepository.findAll().toList()
                .filter { type.isEmpty() || it.action.equals(type, ignoreCase = true) }
                .filter { userId == 0L || it.userId == userId }

        // Get the users, so we can display a name of the user for each entry
        val userIds = entries.map { it.userId }.distinct()
        val users = rolesystem.getUsers(userIds, securityService.getToken())

        // Add a name to each audit entry
        return entries.map { entry: AuditEntry ->

            // There should be exactly one user that matches the userId for the entry
            val user = users.find { it.id == entry.userId }

            // If the user actually exists
            if (user != null) {
                // use their name
                entry.addName(user.name)
            } else {
                // Fallback to '(N/A)' if no user exists
                entry.addName("(N/A)")
            }

        }
    }

    override fun getFilters(authToken: String): Filters {
        val userIds = auditRepository.findUserIdDistinct()
        val actions = auditRepository.findActionDistinct()
        val users = rolesystem.getUsers(userIds, authToken)
        return Filters(users, actions)
    }

}