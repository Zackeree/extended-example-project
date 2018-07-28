package com.example.project.controller.model.user

import com.example.project.contract.user.Create
import com.example.project.contract.user.ErrorTag
import com.example.project.controller.model.BaseCreateForm
import com.example.project.isNullOrBlankOrNotEmail
import com.google.common.collect.HashMultimap

/**
 * Class used to encapsulate data from the front-end.
 * Extends from the [BaseCreateForm] interface
 */
data class CreateForm(
        val username: String?,
        val email: String?,
        val password: String?,
        val passwordConfirm: String?
) : BaseCreateForm<Create.Request> {
    /**
     * Method that converts the [CreateForm] into a
     * [Create.Request] object
     */
    override fun toRequest(): Create.Request {
        return Create.Request(
                username = username!!,
                email = email!!,
                password = password!!,
                passwordConfirm = passwordConfirm!!
        )
    }

    /**
     * * In order to safely use the '!!' operator to ignore the possibility
     * that a variable/object is null we must first ensure this is true
     * by validating the incoming request. Some of this may be repeat code
     * from the contracts, but this is because I didn't realize I would
     * need to do this before executing the command object. We can also
     * check some other basic validation so we don't need to instantiate
     * other objects and use more resources
     */
    fun validateRequest(): HashMultimap<ErrorTag, String>? {
        val errors = HashMultimap.create<ErrorTag, String>()
        if (username.isNullOrBlank())
            errors.put(ErrorTag.USERNAME, "Required field")
        if (email.isNullOrBlankOrNotEmail())
            errors.put(ErrorTag.EMAIL_ADDRESS, "Invalid Email")
        if (password.isNullOrBlank())
            errors.put(ErrorTag.PASSWORD, "Required field")
        if (passwordConfirm.isNullOrBlank())
            errors.put(ErrorTag.PASSWORD_CONFIRM, "Required field")

        if (!errors.isEmpty)
            return errors

        if (!password.equals(passwordConfirm))
            errors.put(ErrorTag.PASSWORD_CONFIRM, "Passwords do not match")

        return if (errors.isEmpty) null else errors
    }
}