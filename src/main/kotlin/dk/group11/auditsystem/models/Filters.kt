package dk.group11.auditsystem.models


data class User(val id: Long, val name: String)

data class Filters(val users: List<User>, val actions: List<String>)