package dk.group11.auditsystem.auditClient

import org.junit.Test

import org.junit.experimental.categories.Category

data class Testdata(val prop2: Int, val prop1: String)

@Category(AuditClientTest::class)
class AuditClientTest {

    @Test
    fun createEntry() {
        val config = AuditConfigProperties(url = "http://localhost:8086")
        val client = AuditClient(config)
        val authToken = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJpZFwiOjIsXCJ1c2VybmFtZVwiOlwic29maWUxMlwifSIsImV4cCI6MTUwOTg2NzEwOX0.ANooUn6yZv_6dOh2ij1Gbek-96trh8qkV7oxsjPUdMg8pbUdFAVV1-NR_ywFPFrAjPg-euHYTKMRlpCyBZ5Grw"
        client.createEntry(action = "testString", data = "this works", authToken = authToken)
        client.createEntry(action = "testInt", data = 2, authToken = authToken )
        client.createEntry(action = "testEmpty", data = "", authToken = authToken)
        client.createEntry(action = "testLong", data = 2L, authToken = authToken)
        client.createEntry(action = "testDouble", data = 3.5, authToken = authToken)
        client.createEntry(action = "testObject", data = Testdata(1,"hello"), authToken = authToken)
        client.createEntry(action = "testDumb", data = true, authToken = authToken)



    }

}