package com.example.project.controller.model.person

import com.example.project.contract.person.Update
import com.example.project.controller.model.BaseForm

/**
 * Class used to encapsulate data from the client
 * Inherits the [BaseForm] interface
 */
data class UpdateForm(
        val id: Long,
        val firstName: String,
        val lastName: String
) : BaseForm<Update.Request> {
    /**
     * Converts the [UpdateForm] to a [Update.Request] object
     */
    override fun toRequest(): Update.Request {
        return Update.Request(
                id = id,
                firstName = firstName,
                lastName = lastName
        )
    }
}