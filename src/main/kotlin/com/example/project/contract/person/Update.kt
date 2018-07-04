package com.example.project.contract.person

import com.example.project.contract.BaseUpdateRequest
import com.example.project.contract.Command
import com.example.project.contract.responder.UpdateResponder
import com.example.project.repository.person.IPersonRepository
import com.example.project.repository.person.Person
import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap

/**
 * Person Update command. Extends the [Command] object
 * @property request the [Update.Request] data class
 * @property responder the [UpdateResponder]
 * @property personRepo the [IPersonRepository]
 */
class Update(
        private val request: Update.Request,
        private val responder: UpdateResponder<ErrorTag>,
        private val personRepo: IPersonRepository
) : Command {
    /**
     * Override of the [Command] object execute method.
     * If the [validateRequest] method returns any errors,
     * respond with the errors. Otherwise, grab and update
     * the person entity and persist the changes
     */
    override fun execute() {
        val errors = validateRequest()
        if (!errors.isEmpty) {
            responder.onFailure(errors)
            return
        }

        val person = personRepo.save(request.createUpdatedEntity(personRepo.findById(request.id).get()))
        responder.onSuccess(person.id)
    }

    /**
     * Method responsible for the constraint checking and validation
     * for the person update request
     */
    private fun validateRequest(): Multimap<ErrorTag, String> {
        with(request) {
            val errors = HashMultimap.create<ErrorTag, String>()

            if (!personRepo.existsById(id))
                errors.put(ErrorTag.ID, "id$id not found")
            if (firstName.isBlank())
                errors.put(ErrorTag.FIRST_NAME, "First Name may not be blank.")
            if (lastName.isBlank())
                errors.put(ErrorTag.LAST_NAME, "Last Name may not be blank.")
            if (firstName.length > 50)
                errors.put(ErrorTag.FIRST_NAME, "First Name must be under 50 characters.")
            if (lastName.length > 50)
                errors.put(ErrorTag.LAST_NAME, "Last Name must be under 50 characters.")

            return errors

        }
    }

    data class Request(
            val id: Long,
            val firstName: String,
            val lastName: String
    ) : BaseUpdateRequest<Person> {
        override fun createUpdatedEntity(e: Person): Person {
            return e.copy(firstName = firstName, lastName = lastName)
        }
    }
}