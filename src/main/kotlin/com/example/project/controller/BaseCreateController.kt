package com.example.project.controller

import com.example.project.controller.model.Result

abstract class BaseCreateController<VIEWMODEL> : BaseRestController() {
    abstract fun execute(model: VIEWMODEL): Result
}