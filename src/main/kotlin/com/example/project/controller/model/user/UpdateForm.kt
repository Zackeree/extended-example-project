package com.example.project.controller.model.user

import com.example.project.contract.user.Update
import com.example.project.controller.model.BaseForm

/**
 * Class used to encapsulate data from the client.
 * Extends the [BaseForm] interface
 */
data class UpdateForm(
        private val id: Long,
        private val username: String,
        private val email: String
) : BaseForm<Update.Request> {
    /**
     * Converts the [UpdateForm] to a [Update.Request] object
     */
    override fun toRequest(): Update.Request {
        return Update.Request(
                id = id,
                username = username,
                email = email
        )
    }
}