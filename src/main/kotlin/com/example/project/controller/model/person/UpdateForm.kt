package com.example.project.controller.model.person

import com.example.project.contract.person.Update
import com.example.project.controller.model.BaseUpdateForm

data class UpdateForm(
        val id: Long,
        val firstName: String,
        val lastName: String
) : BaseUpdateForm<Update.Request> {
    override fun toUpdateRequest(): Update.Request {
        return Update.Request(
                id = id,
                firstName = firstName,
                lastName = lastName
        )
    }
}