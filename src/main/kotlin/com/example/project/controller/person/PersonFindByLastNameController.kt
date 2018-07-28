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
import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
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
) : BasePageController<String?>() {
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
     * with a url of "/users/persons/lastName. It first checks to make sure the
     * request does not contain any null values. If it does not the method calls
     * and executes the [UserPersonWrapper] findByLastName command, which
     * returns a [FindByLastName] command object. The controller then executes
     * the returned command object and responds with the [Result] object
     */
    @GetMapping(value = ["/users/persons/lastName"])
    override fun execute(@RequestParam("lastName") model: String?,
                         @RequestParam("pageSize") pageSize: Int,
                         @RequestParam("pageNumber") pageNumber: Int): Result {
        validateRequest(model)?.let { return Result(null, it.toStringMap()) }

        factoryBeans.getPersonWrapper().factory(userPreconditionFailure()).findByLastName(
                lastName = model!!,
                pageable = PageRequest.of(0,25),
                responder = responder
        ).execute()

        return result
    }

    /**
     * Checks to see if the first name is null or blank. If it is not, it returns
     * null. If it is, it returns a [HashMultimap]
     */
    private fun validateRequest(firstName: String?): Multimap<ErrorTag, String>? {
        val errors = HashMultimap.create<ErrorTag, String>()
        if (firstName.isNullOrBlank())
            errors.put(ErrorTag.FIRST_NAME, "Required field")

        return if (errors.isEmpty) null else errors
    }
}