package com.example.project.controller.security

import com.example.project.contract.Command
import com.example.project.contract.Executable
import com.example.project.contract.SecuredAction
import com.example.project.contract.crud.SimpleResult
import com.google.common.collect.HashMultimap

/**
 * Fake implementation of the [UserContext] object for use in in-memory tests
 */
class FakeUserContext : UserContext {
    var currentRoles = mutableListOf<UserRole>(UserRole.USER)
    private var currentUserId = -1L

    override fun <S, E> require(requiredRoles: List<UserRole>, successCommand: Executable<S, E>): SecuredAction<S, E> {
        return object : SecuredAction<S, E> {
            override fun execute(withAccess: (result: SimpleResult<S, E>) -> Unit): AccessReport? {
                val missingRoles = mutableListOf<UserRole>()
                var failure = false

                requiredRoles.forEach {
                    if (!currentRoles.contains(it)) {
                        failure = true
                        missingRoles.add(it)
                    }
                }
                if (failure) {
                    return AccessReport(missingRoles.toList())
                }
                withAccess(successCommand.execute())
                return null
            }
        }
    }

    override fun require(requiredRoles: List<UserRole>, successCommand: Command, failureCommand: UserPreconditionFailure): Command {
        val missingRoles = mutableListOf<UserRole>()
        var failure = false

        requiredRoles.forEach {
            if (!currentRoles.contains(it)) {
                failure = true
                missingRoles.add(it)
            }
        }

        if (failure) {
            val errors = HashMultimap.create<UserPreconditionFailureTag, String>()
            missingRoles.map {
                errors.put(UserPreconditionFailureTag.MISSING_ROLE, "Role: $it")
            }

            failureCommand.addErrors(errors)
            return failureCommand
        }

        return successCommand
    }

    override fun requireAny(requiredRoles: List<UserRole>, successCommand: Command, failureCommand: UserPreconditionFailure): Command {
        val missingRoles = mutableListOf<UserRole>()

        requiredRoles.forEach {
            if (currentRoles.contains(it)) return successCommand
            else missingRoles.add(it)
        }

        val errors = HashMultimap.create<UserPreconditionFailureTag, String>()
        missingRoles.map {
            errors.put(UserPreconditionFailureTag.MISSING_ROLE, "Role: $it")
        }

        failureCommand.addErrors(errors)
        return failureCommand
    }

    override fun currentUserId(): Long? {
        return if (currentUserId >= 0)
            currentUserId
        else
            null
    }

    fun login(userId: Long) {
        currentUserId = userId
        currentRoles.add(UserRole.USER)
    }

    fun logout() {
        currentUserId = -1
        currentRoles.clear()
    }
}