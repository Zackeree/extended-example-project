package com.example.project.controller.model.user

import com.example.project.contract.user.FindByUsernameOrEmailAndPassword
import com.example.project.controller.model.BaseForm

data class LoginForm(
        val username: String,
        val password: String
) : BaseForm<FindByUsernameOrEmailAndPassword.Request> {
    override fun toRequest(): FindByUsernameOrEmailAndPassword.Request {
        return FindByUsernameOrEmailAndPassword.Request(
                usernameOrEmail = username,
                password = password
        )
    }
}