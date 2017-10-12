package dk.group11.auditsystem.client

import com.github.kittinunf.fuel.Fuel

class AuditClient {
    fun doo() {
        Fuel.get("http://zlepper.dk")
    }
}