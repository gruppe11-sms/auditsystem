package dk.group11.auditsystem.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "audit")
data class ConfigProperties(
        var roleSystemUrl: String = ""
)