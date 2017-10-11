package dk.group11.auditsystem.services

import dk.group11.auditsystem.models.AuditEntry

interface IAuditService {
    fun createEntry(entry: AuditEntry): AuditEntry
    fun getAllEntries(type: String = "", userId: String = ""): Iterable<AuditEntry>
}