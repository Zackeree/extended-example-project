package com.example.project.controller.model.user

import com.example.project.contract.user.Create
import com.example.project.controller.model.BaseCreateForm

/**
 * Class used to encapsulate data from the front-end.
 * Extends from the [BaseCreateForm] interface
 */
data class CreateCreateForm(
        val username: String,
        val email: String,
        val password: String,
        val passwordConfirm: String
) : BaseCreateForm<Create.Request> {
    /**
     * Method that converts the [CreateCreateForm] into a
     * [Create.Request] object
     */
    override fun toRequest(): Create.Request {
        return Create.Request(
                username = username,
                email = email,
                password = password,
                passwordConfirm = passwordConfirm
        )
    }
}