package dk.group11.auditsystem

import dk.group11.auditsystem.clients.RoleClient
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class Startup(private val roleClient: RoleClient) : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        roleClient.ensureRole(ViewerRole, "Audit viewer", "Allows to user to view and query audit entries")
    }

}