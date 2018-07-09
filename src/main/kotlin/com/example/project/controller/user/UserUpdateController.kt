package com.example.project.controller.user

import com.example.project.contract.responder.UpdateResponder
import com.example.project.contract.user.ErrorTag
import com.example.project.contract.user.UserUserWrapper
import com.example.project.controller.BaseRestController
import com.example.project.controller.BaseUpdateController
import com.example.project.controller.model.Result
import com.example.project.controller.model.user.UpdateForm
import com.example.project.toStringMap
import com.google.common.collect.Multimap
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserUpdateController(
        private val userWrapper: UserUserWrapper
) : BaseUpdateController<UpdateForm>() {
    private val responder = object : UpdateResponder<ErrorTag> {
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

    @PutMapping(value = ["/users/{userId}"])
    override fun execute(@PathVariable("userId") id: Long,
                         @RequestBody model: UpdateForm): Result {
        userWrapper.factory(userPreconditionFailure()).update(
                request = model.toRequest(
                        id = id
                ),
                responder = responder
        ).execute()

        return result
    }
}