package com.example.project.controller.person

import com.example.project.contract.person.UserPersonWrapper
import com.example.project.controller.BaseCreateController
import com.example.project.controller.model.Result
import com.example.project.controller.model.person.CreateCreateForm
import com.example.project.toStringMap
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class PersonCreateController(
        private var personWrapper: UserPersonWrapper
) : BaseCreateController<CreateCreateForm>() {
    @PostMapping(value = ["/users/persons"])
    override fun execute(@RequestBody model: CreateCreateForm): Result {
        personWrapper.create(model.toRequest()) {
          it.success?.let { result = Result(data = it) }
          it.error?.let { result = Result(errors = it.toStringMap()) }
        }?.let { result = Result(errors = it.toStringMap()) }
        return result
    }
}