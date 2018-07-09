package com.example.project.controller.model.person

import com.example.project.contract.person.Update
import com.example.project.controller.model.BaseCreateForm
import com.example.project.controller.model.BaseUpdateForm

/**
 * Class used to encapsulate data from the client
 * Inherits the [BaseCreateForm] interface
 */
data class UpdateForm(
        val firstName: String,
        val lastName: String
) : BaseUpdateForm<Update.Request> {
    /**
     * Converts the [UpdateForm] to a [Update.Request] object
     */
    override fun toRequest(id: Long): Update.Request {
        return Update.Request(
                id = id,
                firstName = firstName,
                lastName = lastName
        )
    }
}