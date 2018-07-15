package com.example.project.controller

import com.example.project.controller.model.Result

/**
 * REST retrieve controllers should only ever have a single id
 * as part of the request. As part of our conventions, our execute
 * method (which will have a get mapping annotation) will only need a
 * single parameter (which will come from a path variable from the api
 * url). Extends the [BaseRestController].
 */
abstract class BaseRetrieveController : BaseRestController() {
    abstract fun execute(id: Long): Result
}