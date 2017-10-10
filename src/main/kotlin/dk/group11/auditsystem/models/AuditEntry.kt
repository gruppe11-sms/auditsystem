package dk.group11.auditsystem.models

import java.util.*
import javax.persistence.*

@Entity
data class AuditEntry(
        var timestamp: Date = Date(),
        var userId: String = "",
        var action: String = "",
        @Column(columnDefinition = "TEXT")
        var data: String = "",
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = 0
)
