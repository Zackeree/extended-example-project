package com.example.project.controller.model.person

import com.example.project.contract.person.Create
import com.example.project.contract.person.ErrorTag
import com.example.project.controller.model.BaseCreateForm
import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap

/**
 * Class used to encapsulate data from the client.
 * Implements the [BaseCreateForm] interface
 */
data class CreateForm(
        val userId: Long?,
        val firstName: String?,
        val lastName: String?
) : BaseCreateForm<Create.Request> {
    /**
     * Converts the [CreateForm] into a [Create.Request] object
     */
    override fun toRequest(): Create.Request {
        return Create.Request(
                userId = userId!!,
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
    fun validateRequest(): Multimap<ErrorTag, String>? {
        val errors = HashMultimap.create<ErrorTag, String>()
        if (userId == null || userId == -1L) {
            errors.put(ErrorTag.USER, "User is not logged in")
        }
        if (firstName.isNullOrBlank()) {
            errors.put(ErrorTag.FIRST_NAME, "Required field")
        }
        if (lastName.isNullOrBlank()) {
            errors.put(ErrorTag.LAST_NAME, "Required field")
        }

        return if (errors.isEmpty) null else errors
    }
}