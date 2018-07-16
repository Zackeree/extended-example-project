package com.example.project.controller.person

import com.example.project.contract.person.ErrorTag
import com.example.project.contract.person.PersonInfo
import com.example.project.contract.person.Retrieve
import com.example.project.contract.person.UserPersonWrapper
import com.example.project.contract.responder.RetrieveResponder
import com.example.project.controller.BaseRetrieveController
import com.example.project.controller.model.Result
import com.example.project.controller.spring.FactoryBeans
import com.example.project.repository.person.Person
import com.example.project.toStringMap
import com.google.common.collect.Multimap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

/**
 * [Person] Retrieve Controller. Extends the [BaseRetrieveController] interface.
 * The controller instantiates a concrete [RetrieveResponder] object to handle
 * onFailure and onSuccess scenarios. It also implements the [BaseRetrieveController.execute]
 * method, which handles creating and calling the [UserPersonWrapper.factory] retrieve method.
 * This method is in charge of creating and executing the [Retrieve] command object.
 * @property factoryBeans the [FactoryBeans] factory implementation
 */
@RestController
class PersonRetrieveController(
        private val factoryBeans: FactoryBeans
) : BaseRetrieveController() {
    /**
     * Concrete [RetrieveResponder] object that handles onSuccess and onFailure.
     * On success, the responder will set the result object to have the [PersonInfo]
     * object as its data and a null errors map. On failure, it will set the result's
     * data to null and return a map of errors
     */
    private val responder = object : RetrieveResponder<PersonInfo, ErrorTag> {
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

    /**
     * Override of the [BaseRetrieveController.execute] method. As with all
     * Retrieve Controllers, the execute method has a get mapping annotation
     * with a url of "/users/persons/{personId}" where "{personId}" is a path
     * variable. The method calls and executes the [UserPersonWrapper] retrieve
     * command, which returns a [Retrieve] command object. The controller then executes
     * the returned command object and responds with the [Result] object.
     */
    @GetMapping(value = ["/users/persons/{personId}"])
    override fun execute(@PathVariable id: Long): Result {
        factoryBeans.getPersonWrapper().factory(userPreconditionFailure()).retrieve(
                id = id,
                responder = responder
        ).execute()

        return result
    }
}