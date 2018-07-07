package com.example.project.controller.model.person

import com.example.project.contract.person.Create
import com.example.project.controller.model.BaseForm

data class CreateForm(
        val userId: Long,
        val firstName: String,
        val lastName: String
) : BaseForm<Create.Request> {
    override fun toRequest(): Create.Request {
        return Create.Request(
                userId = userId,
                firstName = firstName,
                lastName = lastName
        )
    }
}