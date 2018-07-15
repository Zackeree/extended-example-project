package com.example.project.controller.model.user

import com.example.project.contract.user.Create
import com.example.project.controller.model.BaseCreateForm

/**
 * Class used to encapsulate data from the front-end.
 * Extends from the [BaseCreateForm] interface
 */
data class CreateCreateForm(
        val username: String?,
        val email: String?,
        val password: String?,
        val passwordConfirm: String?
) : BaseCreateForm<Create.Request> {
    override fun toRequest(): Create.Request {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}