package com.example.project.controller

import com.example.project.controller.model.Result

abstract class BaseUpdateController<VIEWMODEL> : BaseRestController() {
    abstract fun execute(id: Long, model: VIEWMODEL): Result
}