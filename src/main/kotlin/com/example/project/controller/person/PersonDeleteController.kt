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

    @DeleteMapping(value = ["/users/persons/{personId}"])
    override fun execute(@PathVariable("personId") id: Long): Result {
        factoryBeans.getPersonWrapper().factory(userPreconditionFailure()).delete(
                id = id,
                responder = responder
        ).execute()

        return result
    }
}