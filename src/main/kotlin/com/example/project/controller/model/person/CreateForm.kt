package com.example.project.controller.model.person

import com.example.project.contract.person.Create
import com.example.project.controller.model.BaseCreateForm

data class CreateForm(
        val userId: Long,
        val firstName: String,
        val lastName: String
) : BaseCreateForm<Create.Request> {
    override fun toCreateRequest(): Create.Request {
        return Create.Request(
                userId = userId,
                firstName = firstName,
                lastName = lastName
        )
    }
}