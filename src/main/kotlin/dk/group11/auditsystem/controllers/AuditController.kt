package dk.group11.auditsystem.controllers

import dk.group11.auditsystem.models.AuditEntry
import dk.group11.auditsystem.services.AuditService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auditentry")
class AuditController(val auditService: AuditService) {

    @PostMapping
    fun createEntry(@RequestBody entry: AuditEntry): AuditEntry {
        return auditService.createEntry(entry)
    }

    @GetMapping
    fun getAllEntries(
            @RequestParam("type", defaultValue = "") type: String,
            @RequestParam("userid", defaultValue = "") userId: String): Iterable<AuditEntry> {
        return auditService.getAllEntries(type = type, userId = userId)
    }

}