package com.example.project.contract.user

import com.example.project.contract.Command
import com.example.project.repository.user.User
import com.example.project.contract.responder.RetrieveResponder
import com.example.project.repository.user.IUserRepository
import com.google.common.collect.HashMultimap
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

/**
 * [User] FindByUsernameOrEmailAndPassword command. Extends the [Command] object
 * This command object is used as the user login command.
 * @property request the [Request] data class
 * @property responder the [RetrieveResponder] interface
 * @property userRepo the [IUserRepository] interface
 */
class FindByUsernameOrEmailAndPassword(
        private val request: Request,
        private val responder: RetrieveResponder<UserInfo, ErrorTag>,
        private val userRepo: IUserRepository
) : Command {
    /**
     * Override of the [Command] execute method. If the [Request.usernameOrEmail] field
     * is not found in the database (for username or email), respond with an error.
     * Otherwise, grab the [User] entity and check to make sure the password matches the
     * the stored hashed password. If they match, we can log the user in, otherwise, respond
     * with errors.
     */
    override fun execute() {
        if (userRepo.countByEmail(request.usernameOrEmail) == 0 && userRepo.countByUsername(request.usernameOrEmail) == 0) {
            val errors = HashMultimap.create<ErrorTag, String>()
            errors.put(ErrorTag.ID, "Invalid login credentials")
            responder.onFailure(errors)
        } else {
            val theUser = userRepo.findByUsernameOrEmail(request.usernameOrEmail)
            if (theUser == null) {
                val errors = HashMultimap.create<ErrorTag, String>()
                errors.put(ErrorTag.ID, "Invalid login credentials")
                responder.onFailure(errors)
                return
            }
            val passwordEncoder = BCryptPasswordEncoder()
            if (!passwordEncoder.matches(request.password, theUser.password)) {
                val errors = HashMultimap.create<ErrorTag, String>()
                errors.put(ErrorTag.ID, "Invalid login credentials")
                responder.onFailure(errors)
                return
            }
            val theInfo = UserInfo(theUser)
            responder.onSuccess(theInfo)
        }
    }

    /**
     * Data class containing the fields necessary to validate a login request.
     */
    data class Request(
            val usernameOrEmail: String,
            val password: String
    )
}