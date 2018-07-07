package com.example.project.contract.user

import com.example.project.contract.Command
import com.example.project.contract.responder.DeleteResponder
import com.example.project.repository.user.IUserRepository
import com.google.common.collect.HashMultimap

/**
 * User Delete Command. Extends the [Command] object
 * @property id the user id
 * @property responder the [DeleteResponder]
 * @property userRepo the [IUserRepository] interface
 */
class Delete(
        private val id: Long,
        private val responder: DeleteResponder<ErrorTag>,
        private val userRepo: IUserRepository
): Command {
    /**
     * Override of the [Command] object execute method.
     * If the user id does not exist, respond with an error
     * Otherwise, delete the user
     */
    override fun execute() {
        if (!userRepo.existsById(id)) {
            val errors = HashMultimap.create<ErrorTag, String>()
            errors.put(ErrorTag.ID, "User#$id does not exist")
            responder.onFailure(errors)
            return
        }
        val theUser = userRepo.findById(id).get()
        userRepo.delete(theUser)
        responder.onSuccess(theUser.id)
    }
}