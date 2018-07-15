package com.example.project.controller

import com.example.project.controller.security.UserPreconditionFailure
import com.example.project.controller.security.UserPreconditionFailureResponder
import com.example.project.controller.security.UserPreconditionFailureTag
import com.example.project.controller.model.Result
import com.example.project.toStringMap
import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import org.springframework.http.HttpStatus

/**
 * The BaseRestController serves two responsibilities. First and foremost, it
 * has a [Result] object which can be used to respond back to the client-side.
 * It is also responsible for the [userPreconditionFailure] method that returns
 * a new concrete implementation of the [UserPreconditionFailure] interface.
 * This allows all controllers that the BaseRestController to have a concrete
 * responder to authentication error-handling
 */
abstract class BaseRestController {
    var result = Result()

    /**
     * Returns a concrete [UserPreconditionFailure] implementation
     */
    fun userPreconditionFailure(): UserPreconditionFailure {
        return object : UserPreconditionFailure {
            override fun execute() {
                responder.onFailure(errors)
            }

            /**
             * Add an existing multimap of errors to the [UserPreconditionFailure.errors] list
             */
            override fun addErrors(errors: Multimap<UserPreconditionFailureTag, String>) {
                this.errors.putAll(errors)
            }

            /**
             * Add a [Pair] of [UserPreconditionFailureTag] and [String] to the
             * [UserPreconditionFailure.errors] list
             */
            override fun addError(error: Pair<UserPreconditionFailureTag, String>) {
                errors.put(error.first, error.second)
            }

            /**
             * Instantiate a concrete [UserPreconditionFailureResponder]
             * that instantiates a [Result] object with no data, a map of
             * errors, and a status of forbidden
             */
            override val responder: UserPreconditionFailureResponder
                get() = object : UserPreconditionFailureResponder {
                    override fun onFailure(errors: Multimap<UserPreconditionFailureTag, String>) {
                        result = Result(
                                data = null,
                                errors = errors.toStringMap(),
                                status = HttpStatus.FORBIDDEN
                        )
                    }
                }

            /**
             * Return the errors field
             */
            override val errors: Multimap<UserPreconditionFailureTag, String>
                get() = HashMultimap.create<UserPreconditionFailureTag, String>()
        }
    }
}