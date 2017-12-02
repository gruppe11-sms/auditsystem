package dk.group11.auditsystem

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.retry.annotation.EnableRetry

@SpringBootApplication
@EnableRetry
class AuditSystemApplication

fun main(args: Array<String>) {
    SpringApplication.run(AuditSystemApplication::class.java, *args)
}