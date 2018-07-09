package com.example.project.controller

import com.example.project.controller.model.Result

abstract class BasePageController<VIEWMODEL> : BaseRestController() {
    abstract fun execute(model: VIEWMODEL, pageSize: Int, pageNumber: Int): Result
}