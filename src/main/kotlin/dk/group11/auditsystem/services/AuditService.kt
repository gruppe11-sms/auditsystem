package dk.group11.auditsystem.services

import dk.group11.auditsystem.client.RoleSystemClient
import dk.group11.auditsystem.models.AuditEntry
import dk.group11.auditsystem.models.AuditEntryWithName
import dk.group11.auditsystem.models.Filters
import dk.group11.auditsystem.models.User
import dk.group11.auditsystem.repositories.AuditRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuditService(private val auditRepository: AuditRepository, private val security: ISecurityService, private val rolesystem: RoleSystemClient) : IAuditService {
    override fun createEntry(entry: AuditEntry): AuditEntry {
        return auditRepository.save(entry)
    }

    override fun getAllEntries(type: String, userId: Long, authToken: String): List<AuditEntryWithName> {
        var entries = auditRepository.findAll()
                .filter { type.isEmpty() || it.action.equals(type, ignoreCase = true) }
                .filter { userId == 0L || it.userId == userId }

        val userIds = entries.map { it.userId }.distinct()
        val users = rolesystem.getUsers(userIds, authToken)

        return entries.map { entry: AuditEntry ->

            val user = users.find { it.id == entry.userId }

            if (user != null) {
                entry.addName(user.name)
            } else {
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