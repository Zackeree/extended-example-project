package com.example.project.controller.model.person

import com.example.project.contract.person.Create
import com.example.project.controller.model.BaseCreateForm

/**
 * Class used to encapsulate data from the client.
 * Implements the [BaseCreateForm] interface
 */
data class CreateCreateForm(
        val userId: Long,
        val firstName: String,
        val lastName: String
) : BaseCreateForm<Create.Request> {
    /**
     * Converts the [CreateCreateForm] into a [Create.Request] object
     */
    override fun toRequest(): Create.Request {
        return Create.Request(
                userId = userId,
                firstName = firstName,
                lastName = lastName
        )
    }
}