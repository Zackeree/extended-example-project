package com.example.project.controller.model.user

import com.example.project.contract.user.Update
import com.example.project.controller.model.BaseCreateForm
import com.example.project.controller.model.BaseUpdateForm

/**
 * Class used to encapsulate data from the client.
 * Extends the [BaseCreateForm] interface
 */
data class UpdateForm(
        private val username: String,
        private val email: String
) : BaseUpdateForm<Update.Request> {
    /**
     * Converts the [UpdateForm] to a [Update.Request] object
     */
    override fun toRequest(id: Long): Update.Request {
        return Update.Request(
                id = id,
                username = username,
                email = email
        )
    }
}