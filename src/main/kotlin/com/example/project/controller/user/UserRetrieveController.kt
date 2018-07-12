package com.example.project.controller.user

import com.example.project.contract.responder.RetrieveResponder
import com.example.project.contract.user.ErrorTag
import com.example.project.contract.user.UserInfo
import com.example.project.contract.user.UserUserWrapper
import com.example.project.controller.BaseRestController
import com.example.project.controller.BaseRetrieveController
import com.example.project.controller.model.Result
import com.example.project.toStringMap
import com.google.common.collect.Multimap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class UserRetrieveController(
        private val userWrapper: UserUserWrapper
) : BaseRetrieveController() {
    private val responder = object : RetrieveResponder<UserInfo, ErrorTag> {
        override fun onSuccess(t: UserInfo) {
            result = Result(
                    data = t,
                    errors = null
            )
        }

        override fun onFailure(e: Multimap<ErrorTag, String>) {
            result = Result(
                    data = null,
                    errors = e.toStringMap())
        }
    }

    @GetMapping(value = ["/users/{userId}"])
    override fun execute(@PathVariable(value = "userId") id: Long): Result {
        userWrapper.factory(userPreconditionFailure()).retrieve(
                id = id,
                responder = responder
        ).execute()

        return result
    }
}