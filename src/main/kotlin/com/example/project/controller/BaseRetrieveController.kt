package com.example.project.controller

import com.example.project.controller.model.Result

abstract class BaseRetrieveController : BaseRestController() {
    abstract fun execute(id: Long): Result
}