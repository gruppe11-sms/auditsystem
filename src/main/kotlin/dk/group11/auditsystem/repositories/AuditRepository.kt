package dk.group11.auditsystem.repositories

import dk.group11.auditsystem.models.AuditEntry
import org.springframework.data.repository.CrudRepository

interface AuditRepository : CrudRepository<AuditEntry, Long>