package com.example.project.controller.security

import com.example.project.contract.Command
import com.google.common.collect.Multimap
import com.example.project.contract.person.UserPersonWrapper

/**
 * Define a factory method which needs to be implemented by all
 * user wrappers (i.e [UserPersonWrapper]).
 */
interface UserContextWrapper<out T> {
    /**
     * Returns an entity-specific factory that can handle permission and role checking
     */
    fun factory(userPreconditionFailure: UserPreconditionFailure): T
}

/**
 * Implementation of the [Command] object specifically for role/permission
 * checking
 * @property responder a [UserPreconditionFailureResponder]
 * @property errors a [Multimap] of errors
 */
interface UserPreconditionFailure : Command {
    val responder: UserPreconditionFailureResponder
    val errors: Multimap<UserPreconditionFailureTag, String>

    /**
     * Used to add a single error to the [Multimap] before responding
     */
    fun addError(error: Pair<UserPreconditionFailureTag, String>)

    /**
     * Used to add a full list of errors to before responding
     */
    fun addErrors(errors: Multimap<UserPreconditionFailureTag, String>)

    override fun execute() {
        responder.onFailure(errors)
    }
}


/**
 * Responder object used in UserContextWrapper implementations
 */
interface UserPreconditionFailureResponder {
    /**
     * Respond with the precondition failure report
     * @param errors a [Multimap] of errors
     */
    fun onFailure(errors: Multimap<UserPreconditionFailureTag, String>)
}

/**
 * Indicate which security measure tripped the error
 */
enum class UserPreconditionFailureTag {
    MISSING_ROLE,
    MISSING_PERMISSION
}