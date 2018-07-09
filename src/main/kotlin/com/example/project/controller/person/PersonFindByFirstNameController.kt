package com.example.project.controller.person

import com.example.project.contract.person.ErrorTag
import com.example.project.contract.person.PersonInfo
import com.example.project.contract.person.UserPersonWrapper
import com.example.project.contract.responder.PageResponder
import com.example.project.controller.BasePageController
import com.example.project.controller.BaseRestController
import com.example.project.controller.model.Result
import com.example.project.toStringMap
import com.google.common.collect.Multimap
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class PersonFindByFirstNameController(
        private val personWrapper: UserPersonWrapper
) : BasePageController<String>() {
    private val responder = object : PageResponder<PersonInfo, ErrorTag> {
        override fun onSuccess(t: Page<PersonInfo>) {
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

    @GetMapping(value = ["/users/persons/firstName/{firstName}/{pageSize}/{pageNumber}"])
    override fun execute(@PathVariable("firstName") model: String,
                         @PathVariable("pageSize") pageSize: Int,
                         @PathVariable("pageNumber") pageNumber: Int): Result {
        personWrapper.factory(userPreconditionFailure()).findByFirstName(
                firstName = model,
                pageable = PageRequest.of(0, 25),
                responder = responder
        ).execute()

        return result
    }
}