package com.example.project.controller.model.person

import com.example.project.contract.person.ErrorTag
import com.example.project.contract.person.Update
import com.example.project.controller.model.BaseCreateForm
import com.example.project.controller.model.BaseUpdateForm
import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap

/**
 * Class used to encapsulate data from the client
 * Inherits the [BaseCreateForm] interface
 */
data class UpdateForm(
        val firstName: String?,
        val lastName: String?
) : BaseUpdateForm<Update.Request> {
    /**
     * Converts the [UpdateForm] to a [Update.Request] object
     */
    override fun toRequest(id: Long?): Update.Request {
        return Update.Request(
                id = id!!,
                firstName = firstName!!,
                lastName = lastName!!
        )
    }

    /**
     * In order to safely use the '!!' operator to ignore the possibility
     * that a variable/object is null we must first ensure this is true
     * by validating the incoming request. Some of this may be repeat code
     * from the contracts, but this is because I didn't realize I would
     * need to do this before executing the command object. We can also
     * check some other basic validation so we don't need to instantiate
     * other objects and use more resources
     */
    fun validateRequest(id: Long?): Multimap<ErrorTag, String>? {
        val errors = HashMultimap.create<ErrorTag, String>()
        if (id == null)
            errors.put(ErrorTag.ID, "Invalid id")
        if (firstName.isNullOrBlank())
            errors.put(ErrorTag.FIRST_NAME, "Required field")
        if (lastName.isNullOrBlank())
            errors.put(ErrorTag.LAST_NAME, "Required field")

        return if (errors.isEmpty) null else errors
    }
}