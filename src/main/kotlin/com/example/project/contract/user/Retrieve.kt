package com.example.project.contract.user

import com.example.project.contract.Command
import com.example.project.contract.responder.RetrieveResponder
import com.example.project.repository.user.IUserRepository

/**
 * User Retrieve command. Extends the [Command] object.
 * @property id the user id
 * @property responder the [RetrieveResponder]
 * @property userRepo the [IUserRepository] interface
 */
class Retrieve(
        private val id: Long,
        private val responder: RetrieveResponder<UserInfo>,
        private val userRepo: IUserRepository
) : Command {
    /**
     * Override of the [Command] object execute method.
     * If the user does not exist, respond with an error.
     * Otherwise, retrieve the user and adapt it to a
     * [UserInfo] and respond with it
     */
    override fun execute() {
        if (!userRepo.existsById(id)) {
            responder.onFailure("User#$id not found.")
            return
        }
        val theUser = userRepo.findById(id).get()
        val theInfo = UserInfo(user = theUser)
        responder.onSuccess(theInfo)
    }
}