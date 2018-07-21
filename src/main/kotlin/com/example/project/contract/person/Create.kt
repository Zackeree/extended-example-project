package com.example.project.contract.person

import com.example.project.contract.BaseCreateRequest
import com.example.project.contract.Command
import com.example.project.contract.Executable
import com.example.project.contract.crud.SimpleResult
import com.example.project.contract.responder.CreateResponder
import com.example.project.repository.person.IPersonRepository
import com.example.project.repository.person.Person
import com.example.project.repository.user.IUserRepository
import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap

/**
 * [Person] Create command. Extends the [Command] object
 * @property request the [Request] data class
 * @property responder the [CreateResponder] interface
 * @property personRepo the [IPersonRepository] interface
 * @property userRepo the [IUserRepository] interface
 */
class Create(
        private val request: Request,
        private val personRepo: IPersonRepository,
        private val userRepo: IUserRepository
) : Executable<Long, Multimap<ErrorTag, String>> {
    /**
     * Override of the [Command] execute method. Calls the [validateRequest] method
     * that will handle all constraint checking and validations. If validation passes,
     * it will create and persist a person object and associate it with the user that created it.
     * Otherwise, it will respond with a list of the errors
     */
    override fun execute(): SimpleResult<Long, Multimap<ErrorTag, String>> {
        validateRequest()?.let { return SimpleResult(null, it) }
        val person = request.toEntity()
        person.user = userRepo.findById(request.userId).get()
        personRepo.save(person)
        return SimpleResult(person.id, null)
    }

    /**
     * Method responsible for constraint checking and validations for the person
     * create request. It will ensure the user id exists, that the first name is not blank
     * and is under the max length. It then checks the same constraints for the last name
     */
    private fun validateRequest(): Multimap<ErrorTag, String>? {
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

        return if (errors.isEmpty) null else errors
    }

    /**
     * Data class containing all fields necessary for [Person] creation. Implements the
     * [BaseCreateRequest] interface and overrides the [BaseCreateRequest.toEntity] method
     */
    data class Request(
            val userId: Long,
            val firstName: String,
            val lastName: String
    ) : BaseCreateRequest<Person> {
        override fun toEntity(): Person {
            return Person(
                    firstName = firstName,
                    lastName = lastName
            )
        }
    }
}