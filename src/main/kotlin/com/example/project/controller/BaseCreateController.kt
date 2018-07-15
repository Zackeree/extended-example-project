package com.example.project.controller

import com.example.project.controller.model.Result

/**
 * REST Create controllers should only ever have a single request form object
 * As part of the conventions, our execute method (which will have a post mapping
 * annotation) will only need a single parameter (which happens to be whatever form
 * object will be serialized from the request body). Extends the [BaseRestController]
 */
abstract class BaseCreateController<VIEWMODEL> : BaseRestController() {
    abstract fun execute(model: VIEWMODEL): Result
}