package com.example.project.controller.user

import com.example.project.contract.responder.RetrieveResponder
import com.example.project.contract.user.ErrorTag
import com.example.project.contract.user.UserInfo
import com.example.project.contract.user.UserUserWrapper
import com.example.project.repository.user.User
import com.example.project.contract.user.Retrieve
import com.example.project.controller.BaseRetrieveController
import com.example.project.controller.model.Result
import com.example.project.controller.spring.FactoryBeans
import com.example.project.toStringMap
import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

/**
 * [User] Retrieve Controller. Extends the [BaseRetrieveController] interface.
 * The controller instantiates a concrete [RetrieveResponder] object to handle
 * onFailure and onSuccess scenarios. It also implements the [BaseRetrieveController.execute]
 * method, which handles creating and calling the [UserUserWrapper.factory] retrieve method.
 * This method is in charge of creating and executing the [Retrieve] command object
 * @property factoryBeans the [FactoryBeans] factory implementation
 */
@RestController
class UserRetrieveController(
        private val factoryBeans: FactoryBeans
) : BaseRetrieveController() {
    /**
     * Concrete [RetrieveResponder] object that handles onSuccess and onFailure.
     * On success, the responder will set the result object to have the [UserInfo]
     * object as its data and a null errors map. On failure, it will set the result's
     * data to null, and returns a map of the errors
     */
    private val responder = object : RetrieveResponder<UserInfo, ErrorTag> {
        override fun onSuccess(t: UserInfo) {
            result = Result(
                    data = t,
                    errors = null
            )
        }

        override fun onFailure(e: Multimap<ErrorTag, String>) {
            result = Result(
                    data = null,
                    errors = e.toStringMap())
        }
    }

    /**
     * Override of the [BaseRetrieveController.execute] method. As with all
     * Retrieve Controllers, the execute method has a get mapping annotation
     * with a url of "/users/{userId}" where "{userId}" is a path variable.
     * It will first call the [validateRequest] method to make sure the id is
     * not null. If it is not, the method calls and executes the [UserUserWrapper]
     * retrieve command,which returns a [Retrieve] command object. The controller
     * then executesthe returned command object and responds with the [Result] object.
     */
    @GetMapping(value = ["/users/{userId}"])
    override fun execute(@PathVariable(value = "userId") id: Long?): Result {
        validateRequest(id)?.let { return Result(null, it.toStringMap()) }

        factoryBeans.getUserWrapper().factory(userPreconditionFailure()).retrieve(
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