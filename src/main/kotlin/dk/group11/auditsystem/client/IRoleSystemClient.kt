package dk.group11.auditsystem.client

import dk.group11.auditsystem.models.User

interface IRoleSystemClient {
    fun getUsers(userIds: List<Long>, authToken : String): List<User>
}