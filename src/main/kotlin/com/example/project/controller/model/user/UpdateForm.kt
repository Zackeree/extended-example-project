package com.example.project.controller.model.user

import com.example.project.contract.user.Update
import com.example.project.controller.model.BaseUpdateForm

class UpdateForm(
        private val id: Long,
        private val username: String,
        private val email: String
) : BaseUpdateForm<Update.Request> {
    override fun toUpdateRequest(): Update.Request {
        return Update.Request(
                id = id,
                username = username,
                email = email
        )
    }
}