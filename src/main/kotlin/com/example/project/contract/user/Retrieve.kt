package com.example.project.contract.user

import com.example.project.contract.Command
import com.example.project.contract.responder.RetrieveResponder
import com.example.project.contract.user.ErrorTag
import com.example.project.repository.user.IUserRepository
import com.google.common.collect.HashMultimap

/**
 * User Retrieve command. Extends the [Command] object.
 * @property id the user id
 * @property responder the [RetrieveResponder] interface
 * @property userRepo the [IUserRepository] interface
 */
class Retrieve(
        private val id: Long,
        private val responder: RetrieveResponder<UserInfo, ErrorTag>,
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
            val errors = HashMultimap.create<ErrorTag, String>()
            errors.put(ErrorTag.ID, "User Id $id not found")
            responder.onFailure(errors)
            return
        }
        val theUser = userRepo.findById(id).get()
        val theInfo = UserInfo(user = theUser)
        responder.onSuccess(theInfo)
    }
}