package dk.group11.auditsystem.services

import dk.group11.auditsystem.client.RoleSystemClient
import dk.group11.auditsystem.models.AuditEntry
import dk.group11.auditsystem.repositories.AuditRepository
import dk.group11.auditsystem.security.ISecurityService
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

    @Autowired
    lateinit var security: ISecurityService

    @Autowired
    lateinit var rolesystem: RoleSystemClient

    lateinit var auditService: AuditService

    @Autowired
    lateinit var auditRepository: AuditRepository

    @Before
    fun setUp() {
        auditService = AuditService(auditRepository, security, rolesystem)
    }

    @Test
    fun createEntry() {

        val entry = AuditEntry(userId = 1L, action = "test action")

        val created = auditService.createEntry(entry)

        Assert.assertEquals(entry.userId, created.userId)
    }

    @Test
    fun getAllEntries() {
        for (j in 1..10L) {
            for (i in 1..10) {
                val entry = AuditEntry(userId = j, action = "action$i")
                auditService.createEntry(entry)
            }
        }

        val authToken = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJpZFwiOjIsXCJ1c2VybmFtZVwiOlwic29maWUxMlwifSIsImV4cCI6MTUwOTg2NzEwOX0.ANooUn6yZv_6dOh2ij1Gbek-96trh8qkV7oxsjPUdMg8pbUdFAVV1-NR_ywFPFrAjPg-euHYTKMRlpCyBZ5Grw"
        val result = auditService.getAllEntries(userId = 2L, type = "Test2", authToken = authToken).toList()
        Assert.assertEquals(result.size, 10)
    }

}