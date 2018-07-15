package com.example.project.controller

import com.example.project.controller.model.Result

/**
 * REST Page controllers will have a three parameters, whatever data type
 * we are passing in from the client-side, as well as two path variables
 * for page size and page number. All Page controllers will have a get
 * mapping annotation. Extends the [BasePageController].
 */
abstract class BasePageController<VIEWMODEL> : BaseRestController() {
    abstract fun execute(model: VIEWMODEL, pageSize: Int, pageNumber: Int): Result
}