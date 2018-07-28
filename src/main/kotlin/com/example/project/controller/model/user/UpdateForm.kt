package com.example.project.controller.model.user

import com.example.project.contract.user.ErrorTag
import com.example.project.contract.user.Update
import com.example.project.controller.model.BaseCreateForm
import com.example.project.controller.model.BaseUpdateForm
import com.example.project.isNullOrBlankOrNotEmail
import com.google.common.collect.HashMultimap

/**
 * Class used to encapsulate data from the client.
 * Extends the [BaseCreateForm] interface
 */
data class UpdateForm(
        private val username: String?,
        private val email: String?
) : BaseUpdateForm<Update.Request> {
    /**
     * Converts the [UpdateForm] to a [Update.Request] object
     */
    override fun toRequest(id: Long?): Update.Request {
        return Update.Request(
                id = id!!,
                username = username!!,
                email = email!!
        )
    }

    fun validateRequest(id: Long?): HashMultimap<ErrorTag, String>? {
        val errors = HashMultimap.create<ErrorTag, String>()
        if (id == null)
            errors.put(ErrorTag.ID, "Invalid id")
        if (username.isNullOrBlank())
            errors.put(ErrorTag.USERNAME, "Required field")
        if (email.isNullOrBlankOrNotEmail())
            errors.put(ErrorTag.EMAIL_ADDRESS, "Required field")

        return if (errors.isEmpty) null else errors
    }
}