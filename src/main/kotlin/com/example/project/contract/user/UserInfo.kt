package com.example.project.contract.user

import com.example.project.repository.user.User


/**
 * View class for the [User] entity
 */
data class UserInfo(
        val id: Long,
        val username: String,
        val email: String
) {
    constructor(user: User) : this(
            id = user.id,
            username = user.username,
            email = user.email
    )

}