package dk.group11.auditsystem.services

import dk.group11.auditsystem.models.AuditEntry
import dk.group11.auditsystem.repositories.AuditRepository
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner


@RunWith(SpringRunner::class)
@SpringBootTest
@DataJpaTest
internal class AuditServiceTest {

    lateinit var auditService: AuditService

    @Autowired
    lateinit var auditRepository: AuditRepository

    @Before
    fun setUp() {
        auditService = AuditService(auditRepository)
    }

    @Test
    fun createEntry() {

        val entry = AuditEntry(userId = "test user", action = "test action")

        val created = auditService.createEntry(entry)

        Assert.assertEquals(entry.userId, created.userId)
    }

    @Test
    fun getAllEntries() {
        for (j in 0..9) {
            for (i in 0..9) {
                val entry = AuditEntry(userId = "user$j", action = "action$i")
                auditService.createEntry(entry)
            }
        }

        val result = auditService.getAllEntries(userId = "user2").toList()
        Assert.assertEquals(result.size, 10)
    }

}