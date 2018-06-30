package com.example.project.contract.person

import com.example.project.contract.Command
import com.example.project.contract.responder.UpdateResponder
import com.example.project.repository.person.IPersonRepository
import com.example.project.repository.person.Person
import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap

class Update(
        private val request: Update.Request,
        private val responder: UpdateResponder<ErrorTag>,
        private val personRepo: IPersonRepository
) : Command {
    override fun execute() {
        val errors = validateRequest()
        if (!errors.isEmpty) {
            responder.onFailure(errors)
            return
        }

        val updatedPerson = createUpdatedEntity()
        personRepo.save(updatedPerson)
        responder.onSuccess(updatedPerson.id)
    }

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

    private fun createUpdatedEntity(): Person{
        val originalPerson = personRepo.findById(request.id).get()
        with(request) {
            originalPerson.firstName = firstName
            originalPerson.lastName = lastName
        }

        return originalPerson

    }

    data class Request(
            val id: Long,
            val firstName: String,
            val lastName: String
    )
}