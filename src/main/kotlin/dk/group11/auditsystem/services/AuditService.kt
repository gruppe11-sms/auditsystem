package dk.group11.auditsystem.services

import dk.group11.auditsystem.models.AuditEntry
import dk.group11.auditsystem.repositories.AuditRepository
import org.springframework.stereotype.Service

@Service
class AuditService(private val auditRepository: AuditRepository) : IAuditService {
    override fun createEntry(entry: AuditEntry): AuditEntry {
        return auditRepository.save(entry)
    }

    override fun getAllEntries(type: String, userId: String): Iterable<AuditEntry> {
        return auditRepository.findAll()
                .filter { type.isEmpty() || it.action.equals(type, ignoreCase = true) }
                .filter { userId.isEmpty() || it.userId.equals(userId, ignoreCase = true) }

    }

}