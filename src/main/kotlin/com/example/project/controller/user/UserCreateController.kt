package com.example.project.controller.user

import com.example.project.contract.responder.CreateResponder
import com.example.project.contract.user.ErrorTag
import com.example.project.contract.user.UserUserWrapper
import com.example.project.contract.user.Create
import com.example.project.controller.BaseCreateController
import com.example.project.controller.model.Result
import com.example.project.controller.model.user.CreateForm
import com.example.project.controller.spring.FactoryBeans
import com.example.project.toStringMap
import com.example.project.repository.user.User
import com.google.common.collect.Multimap
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

/**
 * [User] Create Controller. Extends the [BaseCreateController] interface
 * The controller instantiates a concrete [CreateResponder] object to handle
 * onFailure and onSuccess scenarios. It also implements the [BaseCreateController.execute]
 * method, which handles creating and calling the [UserUserWrapper.factory] create method.
 * This method is in charge of creating and executing the [Create] command object
 * @property factoryBeans the [FactoryBeans] bean
 */
@RestController
class UserCreateController(
        private val factoryBeans: FactoryBeans
) : BaseCreateController<CreateForm>(){
    /**
     * Concrete [CreateResponder] object that handles onSuccess and onFailure.
     * On success, the responder will set the result object to have the new id
     * as its data and a null errors map. On failure, it will set the result's data
     * to null, and returns a map of the errors
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
     * with a url of "/users". The method calls and executes the [UserUserWrapper]
     * create command, which returns a [Create] command object. The controller
     * then executes the returned command object and responds with the [Result] object
     */
    @PostMapping(value = ["/users"])
    override fun execute(@RequestBody model: CreateForm): Result {
        model.validateRequest()?.let { return Result(null, it.toStringMap()) }

        factoryBeans.getUserWrapper().factory(userPreconditionFailure()).create(
                request = model.toRequest(),
                responder = responder
        ).execute()

        return result
    }

}