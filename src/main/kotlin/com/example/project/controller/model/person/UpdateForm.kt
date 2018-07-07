package com.example.project.controller.model.person

import com.example.project.contract.person.Update
import com.example.project.controller.model.BaseForm

data class UpdateForm(
        val id: Long,
        val firstName: String,
        val lastName: String
) : BaseForm<Update.Request> {
    override fun toRequest(): Update.Request {
        return Update.Request(
                id = id,
                firstName = firstName,
                lastName = lastName
        )
    }
}