package com.example.project.contract.user

import com.example.project.contract.Command
import com.example.project.contract.responder.DeleteResponder
import com.example.project.repository.user.IUserRepository

/**
 * User Delete Command. Extends the [Command] object
 * @property id the user id
 * @property responder the [DeleteResponder]
 * @property userRepo the [IUserRepository] interface
 */
class Delete(
        private val id: Long,
        private val responder: DeleteResponder,
        private val userRepo: IUserRepository
): Command {
    /**
     * Override of the [Command] object execute method.
     * If the user id does not exist, respond with an error
     * Otherwise, delete the user
     */
    override fun execute() {
        if (!userRepo.existsById(id)) {
            responder.onFailure("User#$id does not exist")
            return
        }
        val theUser = userRepo.findById(id).get()
        userRepo.delete(theUser)
        responder.onSuccess(theUser.id)
    }
}