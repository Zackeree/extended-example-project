package com.example.project.controller.person

import com.example.project.contract.person.ErrorTag
import com.example.project.contract.person.UserPersonWrapper
import com.example.project.contract.person.Delete
import com.example.project.contract.responder.DeleteResponder
import com.example.project.controller.BaseDeleteController
import com.example.project.controller.model.Result
import com.example.project.controller.spring.FactoryBeans
import com.example.project.repository.person.Person
import com.example.project.toStringMap
import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

/**
 * [Person] Delete Controller. Extends the [BaseDeleteController] interface.
 * The controller instantiates a concrete [DeleteResponder] object to handle
 * onFailure and onSuccess scenarios. It also implements the [BaseDeleteController.execute]
 * method, which handles creating and calling the [UserPersonWrapper.factory] delete method.
 * This method is in charge of creating and executing the [Delete] command object
 * @property factoryBeans the [FactoryBeans] bean
 */
@RestController
class PersonDeleteController(
        private val factoryBeans: FactoryBeans
) : BaseDeleteController() {
    /**
     * Concrete [DeleteResponder] object that handles onSuccess and onFailure.
     * On success, the responder will set the result object to have the id of
     * the deleted record as its data and a null errors map. On failure, it will
     * set the result's data to null, and return a map of errors
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
     * Delete controllers, the execute method has a delete mapping annotation
     * with a url of "/users/person/{personId}" where "{personId}" is a path
     * variable. It first calls [validateRequest] which checks to see if the id
     * is null or not. If it is not, the method calls and executes the
     * [UserPersonWrapper] delete command, which returns a [Delete] command object.
     * The controller thenexecutes the returned command object and responds with
     * the [Result] object
     */
    @DeleteMapping(value = ["/users/persons/{personId}"])
    override fun execute(@PathVariable("personId") id: Long?): Result {
        validateRequest(id)?.let { return Result(null, it.toStringMap()) }

        factoryBeans.getPersonWrapper().factory(userPreconditionFailure()).delete(
                id = id!!,
                responder = responder
        ).execute()

        return result
    }

    /**
     * Checks to see if the given id is null or not. If it is, it will return
     * a [HashMultimap] otherwise it will return null
     */
    private fun validateRequest(id: Long?): Multimap<ErrorTag, String>? {
        val errors = HashMultimap.create<ErrorTag, String>()
        if (id == null)
            errors.put(ErrorTag.ID, "Invalid id")

        return if (errors.isEmpty) null else errors
    }
}