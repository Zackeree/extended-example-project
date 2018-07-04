package com.example.project.contract.user

import com.example.project.contract.BaseUpdateRequest
import com.example.project.contract.Command
import com.example.project.contract.responder.UpdateResponder
import com.example.project.repository.user.IUserRepository
import com.example.project.repository.user.User
import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap

/**
 * User Update command. Extends the [Command] object
 * @property request the [Update.Request] data class
 * @property responder the [UpdateResponder]
 * @property userRepo the [IUserRepository]
 */
class Update(
        private val request: Update.Request,
        private val responder: UpdateResponder<ErrorTag>,
        private val userRepo: IUserRepository
) : Command {
    /**
     * Override of the [Command] object execute method.
     * If the [validateRequest] method returns any errors,
     * respond with the errors. Otherwise, grab and update
     * the user entity and persist the changes
     */
    override fun execute() {
        val errors = validateRequest()
        if (!errors.isEmpty) {
            responder.onFailure(errors)
            return
        }
        var user = userRepo.findById(request.id).get()
        user = request.createUpdatedEntity(user)
        responder.onSuccess(user.id)
    }

    /**
     * Method responsible for the constraint checking and validation
     * for the user update request
     */
    private fun validateRequest(): Multimap<ErrorTag, String> {
        val errors = HashMultimap.create<ErrorTag, String>()

        with(request) {
            if (!userRepo.existsById(id)) {
                errors.put(ErrorTag.ID, "id$id was not found.")
                return errors
            }
            if (username.isBlank())
                errors.put(ErrorTag.USERNAME, "Username may not be blank.")
            if (userRepo.countByUsername(username) > 0 && username != userRepo.findById(id).get().username)
                errors.put(ErrorTag.USERNAME, "Username is already in use.")
            if (username.length > 100)
                errors.put(ErrorTag.USERNAME, "Username must be less than 100 characters.")
            if (email.isBlank())
                errors.put(ErrorTag.EMAIL_ADDRESS, "Email Address may not be blank.")
            if (userRepo.countByEmail(email) > 0 && email != userRepo.findById(id).get().email)
                errors.put(ErrorTag.EMAIL_ADDRESS, "Email Address is already in use.")
            if (email.length > 100)
                errors.put(ErrorTag.EMAIL_ADDRESS, "Email Address must be less than 100 characters.")
        }

        return errors
    }

    /**
     * Update request data class that implements the [BaseUpdateRequest] interface
     * and overrides the [BaseUpdateRequest.createUpdatedEntity] method
     */
    data class Request(
            val id: Long,
            val username: String,
            val email: String
    ) : BaseUpdateRequest<User> {
        override fun createUpdatedEntity(e: User): User {
            return e.copy(username = username, email = email)
        }
    }
}