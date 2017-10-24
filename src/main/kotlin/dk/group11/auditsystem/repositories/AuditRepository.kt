package dk.group11.auditsystem.repositories

import dk.group11.auditsystem.models.AuditEntry
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface AuditRepository : CrudRepository<AuditEntry, Long>{

    @Query("SELECT DISTINCT userId FROM AuditEntry")
    fun findUserIdDistinct(): List<Long>

    @Query("SELECT DISTINCT action FROM AuditEntry")
    fun findActionDistinct(): List<String>


}