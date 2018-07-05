package com.example.project.controller.model.user

import com.example.project.contract.user.Create
import com.example.project.controller.model.BaseCreateRequest

data class CreateRequest(
        val username: String,
        val email: String,
        val password: String,
        val passwordConfirm: String
) : BaseCreateRequest<Create.Request> {
    override fun toCreateRequest(): Create.Request {
        return Create.Request(
                username = username,
                email = email,
                password = password,
                passwordConfirm = passwordConfirm
        )
    }
}