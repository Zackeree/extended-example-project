package com.example.project.contract.user

import com.example.project.contract.Command
import com.example.project.contract.responder.RetrieveResponder
import com.example.project.repository.user.IUserRepository
import com.google.common.collect.HashMultimap
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class FindByUsernameOrEmailAndPassword(
        private val request: Request,
        private val responder: RetrieveResponder<UserInfo>,
        private val userRepo: IUserRepository
) : Command {
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

    data class Request(
            val usernameOrEmail: String,
            val password: String
    )
}