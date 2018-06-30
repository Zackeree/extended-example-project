package com.example.project.contract.person

import com.example.project.contract.Command
import com.example.project.contract.responder.CreateResponder
import com.example.project.repository.person.IPersonRepository
import com.example.project.repository.person.Person
import com.example.project.repository.user.IUserRepository
import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap

class Create(
        private val request: Request,
        private val responder: CreateResponder<ErrorTag>,
        private val personRepo: IPersonRepository,
        private val userRepo: IUserRepository
) : Command {
    override fun execute() {
        val errors = validateRequest()
        if (errors.isEmpty) {
            val person = request.toEntity()
            person.user = userRepo.findById(request.userId).get()
            personRepo.save(person)
            responder.onSuccess(person.id)
        } else {
            responder.onFailure(errors)
        }
    }

    private fun validateRequest(): Multimap<ErrorTag, String> {
        val errors = HashMultimap.create<ErrorTag, String>()
        with(request) {
            if (!userRepo.existsById(userId))
                errors.put(ErrorTag.USER, "User#$userId does not exist")
            if (firstName.isBlank())
                errors.put(ErrorTag.FIRST_NAME, "First Name may not be blank.")
            if (lastName.isBlank())
                errors.put(ErrorTag.LAST_NAME, "Last Name may not be blank.")
            if (firstName.length > 50)
                errors.put(ErrorTag.FIRST_NAME, "First Name must be under 50 characters")
            if (lastName.length > 50)
                errors.put(ErrorTag.LAST_NAME, "Last Name must be under 50 characters")
        }

        return errors
    }

    data class Request(
            val userId: Long,
            val firstName: String,
            val lastName: String
    ) {
        fun toEntity(): Person {
            return Person(
                    firstName,
                    lastName
            )
        }
    }
}