package com.example.project.controller.user

import com.example.project.contract.responder.UpdateResponder
import com.example.project.contract.user.ErrorTag
import com.example.project.contract.user.UserUserWrapper
import com.example.project.repository.user.User
import com.example.project.contract.user.Update
import com.example.project.controller.BaseUpdateController
import com.example.project.controller.model.Result
import com.example.project.controller.model.user.UpdateForm
import com.example.project.controller.spring.FactoryBeans
import com.example.project.toStringMap
import com.google.common.collect.Multimap
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

/**
 * [User] Update Controller. Extends the [BaseUpdateController] interface.
 * THe controller instantiates a concrete [UpdateResponder] object to handle
 * onFailure and onSuccess scenarios. It also implements the [BaseUpdateController.execute]
 * method, which handles creating and calling the [UserUserWrapper.factory] update method.
 * This method is in charge of creating and executing the [Update] command object.
 * @property factoryBeans the [FactoryBeans] factory implementation
 */
@RestController
class UserUpdateController(
        private val factoryBeans: FactoryBeans
) : BaseUpdateController<UpdateForm>() {
    /**
     * Concrete [UpdateResponder] object that handles onSuccess and onFailure.
     * On success, the responder will set the result object to have the [User] id
     * as its data and a null errors map. On failure, it will set the result's data
     * to null, and returns a map of errors
     */
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

    /**
     * Override of the [BaseUpdateController.execute] method. As with all
     * Update Controllers, the execute method has a put mapping annotation
     * with a url of "/users/{userId}" where "{userId}" is a path variable.
     * The method calls and executes the [UserUserWrapper] update command,
     * which returns a [Update] command object. The controller the executes
     * the returned command object and responds with the [Result] object.
     */
    @PutMapping(value = ["/users/{userId}"])
    override fun execute(@PathVariable("userId") id: Long,
                         @RequestBody model: UpdateForm): Result {
        factoryBeans.getUserWrapper().factory(userPreconditionFailure()).update(
                request = model.toRequest(
                        id = id
                ),
                responder = responder
        ).execute()

        return result
    }
}