package com.example.project.controller

import com.example.project.controller.security.UserPreconditionFailure
import com.example.project.controller.security.UserPreconditionFailureResponder
import com.example.project.controller.security.UserPreconditionFailureTag
import com.example.project.controller.model.Result
import com.example.project.toStringMap
import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import org.springframework.http.HttpStatus

abstract class BaseRestController {
    var result = Result()

    fun userPreconditionFailure(): UserPreconditionFailure {
        return object : UserPreconditionFailure {
            override fun execute() {
                responder.onFailure(errors)
            }

            override fun addErrors(errors: Multimap<UserPreconditionFailureTag, String>) {
                this.errors.putAll(errors)
            }

            override fun addError(error: Pair<UserPreconditionFailureTag, String>) {
                errors.put(error.first, error.second)
            }

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

            override val errors: Multimap<UserPreconditionFailureTag, String>
                get() = HashMultimap.create<UserPreconditionFailureTag, String>()
        }
    }
}