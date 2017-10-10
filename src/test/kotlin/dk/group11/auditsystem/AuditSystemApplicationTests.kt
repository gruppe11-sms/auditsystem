package dk.group11.auditsystem

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
@DataJpaTest
class AuditSystemApplicationTests {

    @Test
    fun contextLoads() {
    }

}
