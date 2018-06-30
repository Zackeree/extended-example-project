package com.example.project.contract.user

import com.example.project.contract.BaseCreateRequest
import com.example.project.contract.Command
import com.example.project.contract.responder.CreateResponder
import com.example.project.repository.user.IUserRepository
import com.example.project.repository.user.User
import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap

/**
 * [User] Create command. Extends from [Command] object
 * @property request the [Request] data class
 * @property responder the [CreateResponder] interface
 * @property userRepo the [IUserRepository] interface
 */
class Create(
        private val request: Request,
        private val responder: CreateResponder<ErrorTag>,
        private val userRepo: IUserRepository
) : Command {
    /**
     * Override of the [Command] execute method. Calls the [validateRequest] method
     * that will handle all constraint checking and validations. If validation passes,
     * it will create and persist the user object, otherwise it will respond with
     * the a list of the errors
     */
    override fun execute() {
        val errors = validateRequest()
        if (!errors.isEmpty) {
            responder.onFailure(errors)
        } else {
            val newUser = userRepo.save(request.toEntity())
            responder.onSuccess(newUser.id)
        }
    }

    /**
     * Method responsible for constraint checking and validations for the user
     * create request
     */
    private fun validateRequest(): Multimap<ErrorTag, String> {
        val errors = HashMultimap.create<ErrorTag, String>()
        with(request) {
            if (username.isBlank())
                errors.put(ErrorTag.USERNAME, "Username may not be blank.")
            if (userRepo.countByUsername(username) > 0)
                errors.put(ErrorTag.USERNAME, "Username is already in use.")
            if (username.length > 100)
                errors.put(ErrorTag.USERNAME, "Username must be less than 100 characters.")
            if (email.isBlank())
                errors.put(ErrorTag.EMAIL_ADDRESS, "Email Address may not be blank.")
            if (userRepo.countByEmail(email) > 0)
                errors.put(ErrorTag.EMAIL_ADDRESS, "Email Address is already in use.")
            if (email.length > 100)
                errors.put(ErrorTag.EMAIL_ADDRESS, "Email Address must be less than 100 characters.")
            if (password != passwordConfirm)
                errors.put(ErrorTag.PASSWORD_CONFIRM, "Passwords do not match.")

        }

        return errors
    }


    /**
     * Data class containing all fields necessary for user creation. Has a helper method to adapt the
     * class into a [User] object to persist it easily
     */
    data class Request(
            val username: String,
            val email: String,
            val password: String,
            val passwordConfirm: String
    ) : BaseCreateRequest<User> {
        override fun toEntity(): User {
            return User(
                    username,
                    email,
                    password
            )
        }
    }
}