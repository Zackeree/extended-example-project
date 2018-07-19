package com.example.project.controller.person

import com.example.project.contract.person.ErrorTag
import com.example.project.contract.person.Update
import com.example.project.contract.person.UserPersonWrapper
import com.example.project.contract.responder.UpdateResponder
import com.example.project.controller.BaseUpdateController
import com.example.project.controller.model.Result
import com.example.project.controller.model.person.UpdateForm
import com.example.project.controller.spring.FactoryBeans
import com.example.project.repository.person.Person
import com.example.project.toStringMap
import com.google.common.collect.Multimap
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

/**
 * [Person] Update Controller. Extends the [BaseUpdateController] interface.
 * The controller instantiates a concrete [UpdateResponder] object to handle
 * onFailure and onSuccess scenarios. It also implements the [BaseUpdateController.execute]
 * method, which handles creating and calling the [UserPersonWrapper.factory] update method.
 * This method is in charge of creating and executing the [Update] command object
 * @property factoryBeans the [FactoryBeans] bean
 */
@RestController
class PersonUpdateController(
        private var factoryBeans: FactoryBeans
) : BaseUpdateController<UpdateForm>() {
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

    @PutMapping(value = ["/users/persons/{personId}"])
    override fun execute(@PathVariable("personId") id: Long, @RequestBody model: UpdateForm): Result {
        factoryBeans.getPersonWrapper().factory(userPreconditionFailure()).update(
                request = model.toRequest(
                        id = id
                ),
                responder = responder
        ).execute()

        return result
    }
}