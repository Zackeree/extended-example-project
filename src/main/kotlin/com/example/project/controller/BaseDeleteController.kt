package com.example.project.controller

import com.example.project.controller.model.Result

abstract class BaseDeleteController : BaseRestController() {
    abstract fun execute(id: Long): Result
}