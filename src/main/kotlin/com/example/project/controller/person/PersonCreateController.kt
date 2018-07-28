package com.example.project.controller.person

import com.example.project.contract.person.ErrorTag
import com.example.project.contract.person.UserPersonWrapper
import com.example.project.contract.person.Create
import com.example.project.contract.responder.CreateResponder
import com.example.project.controller.BaseCreateController
import com.example.project.controller.model.Result
import com.example.project.controller.model.person.CreateForm
import com.example.project.controller.spring.FactoryBeans
import com.example.project.repository.person.Person
import com.example.project.toStringMap
import com.google.common.collect.Multimap
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

/**
 * [Person] Create Controller. Extends the [BaseCreateController] interface.
 * The controller instantiates a concrete [CreateResponder] object to handle
 * onFailure and onSuccess scenarios. It also implements the [BaseCreateController.execute]
 * method, which handles creating and calling the [UserPersonWrapper.factory] create method.
 * This method is in charge of creating and executing the [Create] command object
 * @property factoryBeans the [FactoryBeans] bean
 */
@RestController
class PersonCreateController(
        private val factoryBeans: FactoryBeans
) : BaseCreateController<CreateForm>() {
    /**
     * Concrete [CreateResponder] object that handles onSuccess and onFailure.
     * On success, the responder will set the result object to have the new id
     * as its data and a null errors map. On failure, it will set the result's
     * data to null, and return a map of errors
     */
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

    /**
     * Override of the [BaseCreateController.execute] method. As with all
     * Create controllers, the execute method has a post mapping annotation
     * with a url of "/users/persons". It will first make sure the request does not
     * have any null values (and other basic validations). If it does not, the
     * method calls and executes the [UserPersonWrapper]create command, which
     * returns a [Create] command object. The controller then executes the
     * returned command object and responds with the [Result] object
     */
    @PostMapping(value = ["/users/persons"])
    override fun execute(@RequestBody model: CreateForm): Result {
        model.validateRequest()?.let { return Result(null, it.toStringMap()) }

        factoryBeans.getPersonWrapper().factory(userPreconditionFailure()).create(
                request = model.toRequest(),
                responder = responder
        ).execute()

        return result
    }
}