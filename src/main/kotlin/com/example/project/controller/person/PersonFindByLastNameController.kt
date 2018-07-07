package com.example.project.controller.person

import com.example.project.contract.person.ErrorTag
import com.example.project.contract.person.PersonInfo
import com.example.project.contract.person.UserPersonWrapper
import com.example.project.contract.responder.PageResponder
import com.example.project.controller.BaseRestController
import com.example.project.controller.model.Result
import com.example.project.toStringMap
import com.google.common.collect.Multimap
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PersonFindByLastNameController(
        private var personWrapper: UserPersonWrapper
) : BaseRestController<String>() {
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

    @GetMapping(value = ["/users/persons/lastName/{lastName}"])
    override fun execute(model: String): Result {
        personWrapper.factory(userPreconditionFailure).findByLastName(
                lastName = model,
                pageable = PageRequest.of(0,25),
                responder = responder
        ).execute()

        return result
    }
}