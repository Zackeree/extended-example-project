package com.example.project.controller.person

import com.example.project.contract.person.ErrorTag
import com.example.project.contract.person.UserPersonWrapper
import com.example.project.contract.responder.CreateResponder
import com.example.project.controller.BaseRestController
import com.example.project.controller.model.Result
import com.example.project.controller.model.person.CreateForm
import com.example.project.toStringMap
import com.google.common.collect.Multimap
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class PersonCreateController(
        private var personWrapper: UserPersonWrapper
) : BaseRestController<CreateForm>() {
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

    @PostMapping
    override fun execute(@RequestBody model: CreateForm): Result {
        personWrapper.factory(userPreconditionFailure).create(
                request = model.toRequest(),
                responder = responder
        ).execute()

        return result
    }
}