package com.example.project.contract.user

import com.example.project.contract.Command
import com.example.project.contract.responder.RetrieveResponder
import com.example.project.repository.user.IUserRepository
import com.google.common.collect.HashMultimap

/**
 * User FindByUsernameOrEmail command. Extends the [Command] object
 */
class FindByUsernameOrEmail(
        private val target: String,
        private val responder: RetrieveResponder<UserInfo>,
        private val userRepo: IUserRepository
) : Command {
    /**
     * Override of the [Command] object execute method.
     * If the input does not match any usernames or emails in
     * the database, respond with an error. Otherwise, grab
     * the user, adapt it to a [UserInfo], and respond with it
     */
    override fun execute() {
        if (userRepo.countByUsername(target) == 0 && userRepo.countByEmail(target) == 0) {
            val errors = HashMultimap.create<ErrorTag, String>()
            errors.put(ErrorTag.ID, "User not found")
            responder.onFailure(errors)
            return
        } else {
            val theUser = userRepo.findByUsernameOrEmail(target)
            if (theUser == null) {
                val errors = HashMultimap.create<ErrorTag, String>()
                errors.put(ErrorTag.ID, "User not found")
                return
            }
            val theInfo = UserInfo(theUser)
            responder.onSuccess(theInfo)
        }
    }
}