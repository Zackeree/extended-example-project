package com.example.project.contract.user

import com.example.project.repository.user.User
import java.util.regex.Pattern


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

    companion object {
        fun isEmailValid(email: String): Boolean {
            return Pattern.compile(
                    "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                            + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                            + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                            + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
            ).matcher(email).matches()
        }
    }
}