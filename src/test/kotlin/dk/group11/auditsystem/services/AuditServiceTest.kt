package dk.group11.auditsystem.services

import com.nhaarman.mockito_kotlin.anyOrNull
import com.nhaarman.mockito_kotlin.mock
import dk.group11.auditsystem.clients.IAuditClient
import dk.group11.auditsystem.clients.IRoleSystemClient
import dk.group11.auditsystem.models.AuditEntry
import dk.group11.auditsystem.models.User
import dk.group11.auditsystem.repositories.AuditRepository
import dk.group11.auditsystem.security.SecurityService
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

val authToken = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJpZFwiOjIsXCJ1c2VybmFtZVwiOlwic29maWUxMlwifSIsImV4cCI6MTUwOTg2NzEwOX0.ANooUn6yZv_6dOh2ij1Gbek-96trh8qkV7oxsjPUdMg8pbUdFAVV1-NR_ywFPFrAjPg-euHYTKMRlpCyBZ5Grw"

@RunWith(SpringRunner::class)
@SpringBootTest
@DataJpaTest
internal class AuditServiceTest {

    lateinit var auditService: AuditService

    @Autowired
    lateinit var auditRepository: AuditRepository

    @Before
    fun setUp() {
        val client = Mockito.mock(IRoleSystemClient::class.java)
        Mockito.`when`(client.getUsers(Mockito.anyList() as List<Long>, Mockito.anyString())).then {
            listOf(User(2, "testuser"))
        }
        val auditClient = mock<IAuditClient> {
            on { createEntry(anyOrNull(), anyOrNull()) }.then { }
        }

        val securityService = Mockito.mock(SecurityService::class.java)
        Mockito.`when`(securityService.getToken()).thenReturn(authToken)
        auditService = AuditService(auditRepository, client, auditClient, securityService)
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
                val entry = AuditEntry(userId = j, action = "Test$i")
                auditService.createEntry(entry)
            }
        }


        val result = auditService.getAllEntries(userId = 2L, type = "Test2").toList()
        Assert.assertEquals(1, result.size)
    }

}