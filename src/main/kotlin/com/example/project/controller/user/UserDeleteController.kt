package com.example.project.controller.user

import com.example.project.contract.responder.DeleteResponder
import com.example.project.contract.user.ErrorTag
import com.example.project.contract.user.UserUserWrapper
import com.example.project.controller.BaseRestController
import com.example.project.controller.model.Result
import com.example.project.toStringMap
import com.google.common.collect.HashMultimap
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserDeleteController(
        private var userWrapper: UserUserWrapper
) : BaseRestController<Long>() {
    private val responder = object : DeleteResponder<ErrorTag> {
        override fun onSuccess(t: Long) {
            result = Result(
                    data = t,
                    errors = null
            )
        }

        override fun onFailure(e: HashMultimap<ErrorTag, String>) {
            result = Result(
                    data = null,
                    errors = e.toStringMap()
            )
        }
    }

    @DeleteMapping(value = ["/users/{userId}"])
    override fun execute(model: Long): Result {
        userWrapper.factory(userPreconditionFailure).delete(
                id = model,
                responder = responder
        ).execute()

        return result
    }
}