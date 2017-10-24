package dk.group11.auditsystem.services

import dk.group11.auditsystem.models.AuditEntry
import dk.group11.auditsystem.models.AuditEntryWithName
import dk.group11.auditsystem.models.Filters

interface IAuditService {
    fun createEntry(entry: AuditEntry): AuditEntry
    fun getAllEntries(type: String = "", userId: Long = 0, authToken: String): List<AuditEntryWithName>
    fun getFilters(authToken: String): Filters
}