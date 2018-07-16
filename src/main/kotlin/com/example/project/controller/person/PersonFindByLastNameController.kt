package com.example.project.controller.person

import com.example.project.contract.person.ErrorTag
import com.example.project.contract.person.PersonInfo
import com.example.project.contract.person.UserPersonWrapper
import com.example.project.contract.person.FindByLastName
import com.example.project.contract.responder.PageResponder
import com.example.project.controller.BasePageController
import com.example.project.controller.model.Result
import com.example.project.controller.spring.FactoryBeans
import com.example.project.repository.person.Person
import com.example.project.toStringMap
import com.google.common.collect.Multimap
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

/**
 * [Person] FindByLastName Page Controller. Extends the [BasePageController]
 * interface. The controller instantiates a concrete [PageResponder] object
 * to handle onFailure and onSuccess scenarios. It also implements the
 * [BasePageController.execute] method, which handles creating and calling the
 * [UserPersonWrapper.factory] findByLastName method. This method is in charge of
 * creating and executing the [FindByLastName] command object
 * @property factoryBeans the [FactoryBeans] bean
 */
@RestController
class PersonFindByLastNameController(
        private val factoryBeans: FactoryBeans
) : BasePageController<String>() {
    /**
     * Concrete [PageResponder] object that handles onSucces and onFailure.
     * On success, the responder will set the result object to have the [Page]
     * of [PersonInfo] objects as its data and a null errors map. On failure, it
     * will set the result's data to null, and return a mqp of errors
     */
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

    /**
     * Override of the [BasePageController.execute] method. As with all
     * Page Controllers, the execute method has a get mapping annotation
     * with a url of "/users/persons/lastName/{lastName}/{pageSize}/{pageNumber}
     * where "{lastName}", "{pageSize}", and "{pageNumber}" are all path variable.
     * The method calls and executes the [UserPersonWrapper] findByLastName command,
     * which returns a [FindByLastName] command object. The controller then executes
     * the returned command object and responds with the [Result] object
     */
    @GetMapping(value = ["/users/persons/lastName/{lastName}/{pageSize}/{pageNumber}"])
    override fun execute(@PathVariable("lastName") model: String,
                         @PathVariable("pageSize") pageSize: Int,
                         @PathVariable("pageNumber") pageNumber: Int): Result {
        factoryBeans.getPersonWrapper().factory(userPreconditionFailure()).findByLastName(
                lastName = model,
                pageable = PageRequest.of(0,25),
                responder = responder
        ).execute()

        return result
    }
}