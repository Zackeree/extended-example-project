package com.example.project.controller.person

import com.example.project.contract.person.PersonInfo
import com.example.project.contract.person.UserPersonWrapper
import com.example.project.contract.responder.RetrieveResponder
import com.example.project.contract.user.ErrorTag
import com.example.project.controller.BaseRestController
import com.example.project.controller.model.Result
import com.example.project.toStringMap
import com.google.common.collect.Multimap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class PersonRetrieveController(
        private val personWrapper: UserPersonWrapper
) : BaseRestController<Long>() {
    private val responder = object : RetrieveResponder<PersonInfo> {
        override fun onSuccess(t: PersonInfo) {
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

    @GetMapping(value = ["/users/persons/{personId}"])
    override fun execute(@PathVariable model: Long): Result {
        personWrapper.factory(userPreconditionFailure).retrieve(
                id = model,
                responder = responder
        ).execute()

        return result
    }
}