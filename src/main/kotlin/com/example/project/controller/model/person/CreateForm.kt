package com.example.project.controller.model.person

import com.example.project.contract.person.Create
import com.example.project.controller.model.BaseForm

/**
 * Class used to encapsulate data from the client.
 * Implements the [BaseForm] interface
 */
data class CreateForm(
        val userId: Long,
        val firstName: String,
        val lastName: String
) : BaseForm<Create.Request> {
    /**
     * Converts the [CreateForm] into a [Create.Request] object
     */
    override fun toRequest(): Create.Request {
        return Create.Request(
                userId = userId,
                firstName = firstName,
                lastName = lastName
        )
    }
}