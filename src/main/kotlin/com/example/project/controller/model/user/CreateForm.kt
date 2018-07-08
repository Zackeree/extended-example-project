package com.example.project.controller.model.user

import com.example.project.contract.user.Create
import com.example.project.controller.model.BaseForm

/**
 * Class used to encapsulate data from the front-end.
 * Extends from the [BaseForm] interface
 */
data class CreateForm(
        val username: String,
        val email: String,
        val password: String,
        val passwordConfirm: String
) : BaseForm<Create.Request> {
    /**
     * Method that converts the [CreateForm] into a
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