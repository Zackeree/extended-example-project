package com.example.project.controller.user

import com.example.project.contract.responder.CreateResponder
import com.example.project.contract.user.ErrorTag
import com.example.project.contract.user.UserUserWrapper
import com.example.project.controller.BaseRestController
import com.example.project.controller.model.Result
import com.example.project.controller.model.user.CreateRequest
import com.example.project.toStringMap
import com.google.common.collect.Multimap
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserCreateController(
        private val userWrapper: UserUserWrapper
) : BaseRestController<CreateRequest>(){
    private val responder = object : CreateResponder<ErrorTag> {
        override fun onSuccess(t: Long) {
            result = Result(
                    data = t,
                    errors = null
            )
        }

        override fun onFailure(e: Multimap<ErrorTag, String>) {
            result = Result(
                    data = null,
                    errors = e.toStringMap()
            )
        }
    }

    @PostMapping(value = ["/users"])
    override fun execute(@RequestBody model: CreateRequest): Result {
        userWrapper.factory(userPreconditionFailure).create(
                request = model.toCreateRequest(),
                responder = responder
        ).execute()

        return result
    }

}