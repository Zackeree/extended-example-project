package com.example.project.controller

import com.example.project.controller.model.Result

/**
 * REST Delete controllers should only ever have a single request form object
 * As part of the conventions, our execute method (which will have a delete
 * mapping annotation) will only need a single parameter (which happens to be
 * the path variable from the api url). Extends the [BaseRestController]
 */
abstract class BaseDeleteController : BaseRestController() {
    abstract fun execute(id: Long?): Result
}