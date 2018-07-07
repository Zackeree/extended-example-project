package com.example.project.controller.model.user

import com.example.project.contract.user.Create
import com.example.project.controller.model.BaseForm

data class CreateForm(
        val username: String,
        val email: String,
        val password: String,
        val passwordConfirm: String
) : BaseForm<Create.Request> {
    override fun toRequest(): Create.Request {
        return Create.Request(
                username = username,
                email = email,
                password = password,
                passwordConfirm = passwordConfirm
        )
    }
}