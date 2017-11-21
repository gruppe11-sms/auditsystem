package dk.group11.auditsystem.clients

import dk.group11.auditsystem.models.User

interface IRoleSystemClient {
    fun getUsers(userIds: List<Long>, authToken : String): List<User>
}