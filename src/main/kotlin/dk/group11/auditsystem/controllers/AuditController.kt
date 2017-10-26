package dk.group11.auditsystem.controllers

import dk.group11.auditsystem.exceptions.BadRequestException
import dk.group11.auditsystem.models.AuditEntry
import dk.group11.auditsystem.models.AuditEntryWithName
import dk.group11.auditsystem.models.Filters
import dk.group11.auditsystem.services.IAuditService
import dk.group11.auditsystem.security.ISecurityService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auditentry")
class AuditController(val auditService: IAuditService, val security: ISecurityService) {



    @PostMapping
    fun createEntry(@RequestBody entry: AuditEntry): AuditEntry {
        val requestEntry = entry.copy(userId = security.getId())
        println(requestEntry)
        return auditService.createEntry(requestEntry)
    }

    @GetMapping
    fun getAllEntries(
            @RequestParam("action", defaultValue = "") action: String,
            @RequestParam("userId", defaultValue = "") userId: String,
            @RequestHeader("Authorization") auth: String): List<AuditEntryWithName> {
        var id = userId.toLongOrNull()
        if (id == null) {
            if (userId.isBlank()) {
                id = 0L
            } else {
                throw BadRequestException("Cannot cast 'userid' to Long")
            }
        }
        return auditService.getAllEntries(type = action, userId = id, authToken = auth)
    }

    @GetMapping("/filters")

    fun getFilters(@RequestHeader("Authorization") auth: String): Filters {
        return auditService.getFilters(auth)
    }


}