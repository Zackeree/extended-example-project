package com.example.project.controller.security

import com.example.project.contract.Command
import com.example.project.repository.user.IUserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder

class UserContextImpl(
        var userRepo: IUserRepository
) : UserContext {

    private val authentication = SecurityContextHolder.getContext().authentication

    override fun require(requiredRoles: List<UserRole>, successCommand: Command, failureCommand: UserPreconditionFailure): Command {
        val authorities = arrayListOf<SimpleGrantedAuthority>()
        authentication.authorities.forEach { authorities.add(SimpleGrantedAuthority(it.authority)) }

        if (authentication is AuthenticatedUserToken) {
            val token = authentication
            if (token.userId == null || !userRepo.existsById(token.userId)) {
                failureCommand.addError(Pair(UserPreconditionFailureTag.MISSING_ROLE, "User is not Logged in."))
                return failureCommand
            }

            val user = userRepo.findById(token.userId).get()

            var hasAllRoles = true
            if (!requiredRoles.isEmpty()) {
                requiredRoles.forEach { role ->
                    var hasThisRole = user.roles.any { it.role == role.name }
                    if (!hasThisRole) {
                        hasAllRoles = false
                        failureCommand.addError(Pair(UserPreconditionFailureTag.MISSING_ROLE, "Missing Role(${role.name}"))
                    }
                }
            }

            return if (hasAllRoles) successCommand else failureCommand
        } else {
            return failureCommand
        }
    }

    override fun requireAny(requiredRoles: List<UserRole>, successCommand: Command, failureCommand: UserPreconditionFailure): Command {
        val authorities = arrayListOf<SimpleGrantedAuthority>()
        authentication.authorities.forEach { authorities.add(SimpleGrantedAuthority(it.authority)) }

        if (authentication is AuthenticatedUserToken) {
            val token = authentication
            if (token.userId == null || !userRepo.existsById(token.userId)) {
                failureCommand.addError(Pair(UserPreconditionFailureTag.MISSING_ROLE, "User is not Logged in."))
                return failureCommand
            }

            val user = userRepo.findById(token.userId).get()

            var hasAnyRoles = false
            if (!requiredRoles.isEmpty()) {
                hasAnyRoles = user.roles.any { role ->
                    requiredRoles.any {
                        it.name == role.role
                    }
                }
            } else {
                hasAnyRoles = true
            }

            return if (hasAnyRoles) successCommand else failureCommand
        } else {
            return failureCommand
        }
    }

    override fun currentUserId(): Long? {
        return if (authentication is AuthenticatedUserToken) authentication.userId else -1L
    }
}