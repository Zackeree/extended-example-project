package com.example.project.controller.user

import com.example.project.contract.responder.DeleteResponder
import com.example.project.contract.user.ErrorTag
import com.example.project.contract.user.UserUserWrapper
import com.example.project.contract.user.Delete
import com.example.project.controller.BaseDeleteController
import com.example.project.repository.user.User
import com.example.project.controller.model.Result
import com.example.project.controller.spring.FactoryBeans
import com.example.project.toStringMap
import com.google.common.collect.HashMultimap
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

/**
 * [User] Delete Controller. Extends the [BaseDeleteController] interface
 * The controller instantiates a concrete [DeleteResponder] object to handle
 * onFailure and onSuccess scenarios. It also implements the [BaseDeleteController.execute]
 * method, which handles creating and calling the [UserUserWrapper.factory] delete method.
 * This method is in charge of creating and executing the [Delete] command object
 * @property factoryBeans the [FactoryBeans]
 */
@RestController
class UserDeleteController(
        private val factoryBeans: FactoryBeans
) : BaseDeleteController() {
    /**
     * Concrete [DeleteResponder] object that handles onSuccess and onFailure.
     * On success, the responder will set the result object's data to have id
     * we deleted and a null errors map. On failure, it will set the result's data
     * to null, and return a map of the errors.
     */
    private val responder = object : DeleteResponder<ErrorTag> {
        override fun onSuccess(t: Long) {
            result = Result(
                    data = t,
                    errors = null
            )
        }

        override fun onFailure(e: HashMultimap<ErrorTag, String>) {
            result = Result(
                    data = null,
                    errors = e.toStringMap()
            )
        }
    }

    /**
     * Override of the [BaseDeleteController.execute] method. As with all
     * Delete Controllers, the execute method has a delete mapping annotation
     * with a url of "/users/{userId}" where {userId} is a path variable. The
     * method calls and executes the [UserUserWrapper] delete command, which
     * returns a [Delete] command object. The controller then executes the
     * returned command object and responds with the [Result] object.
     */
    @DeleteMapping(value = ["/users/{userId}"])
    override fun execute(@PathVariable("userId") id: Long?): Result {
        validateRequest(id)?.let { return Result(null, it.toStringMap()) }

        factoryBeans.getUserWrapper().factory(userPreconditionFailure()).delete(
                id = id!!,
                responder = responder
        ).execute()

        return result
    }

    private fun validateRequest(id: Long?): HashMultimap<ErrorTag, String>? {
        val errors = HashMultimap.create<ErrorTag, String>()
        if (id == null)
            errors.put(ErrorTag.ID, "Invalid id")

        return if (errors.isEmpty) null else errors
    }
}