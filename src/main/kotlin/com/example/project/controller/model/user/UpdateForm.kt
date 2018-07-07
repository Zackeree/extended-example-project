package com.example.project.controller.model.user

import com.example.project.contract.user.Update
import com.example.project.controller.model.BaseForm

data class UpdateForm(
        private val id: Long,
        private val username: String,
        private val email: String
) : BaseForm<Update.Request> {
    override fun toRequest(): Update.Request {
        return Update.Request(
                id = id,
                username = username,
                email = email
        )
    }
}