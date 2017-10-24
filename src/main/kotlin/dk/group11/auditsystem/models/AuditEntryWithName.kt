package dk.group11.auditsystem.models

import java.util.*

data class AuditEntryWithName(
        val name: String,
        val timestamp: Date,
        val userId: Long,
        val action: String,
        val data: String,
        val id: Long
)