package com.example.project.controller.person

import com.example.project.contract.person.ErrorTag
import com.example.project.contract.person.UserPersonWrapper
import com.example.project.contract.responder.UpdateResponder
import com.example.project.controller.BaseRestController
import com.example.project.controller.model.Result
import com.example.project.controller.model.person.UpdateForm
import com.example.project.toStringMap
import com.google.common.collect.Multimap
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PersonUpdateController(
        private var personWrapper: UserPersonWrapper
) : BaseRestController<UpdateForm>() {
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

    @PutMapping(value = ["/users/persons/{personId}"])
    override fun execute(model: UpdateForm): Result {
        personWrapper.factory(userPreconditionFailure).update(
                request = model.toRequest(),
                responder = responder
        ).execute()

        return result
    }
}