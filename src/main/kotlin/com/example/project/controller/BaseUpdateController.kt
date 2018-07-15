package com.example.project.controller

import com.example.project.controller.model.Result

/**
 * REST Update controllers should only ever have two parameters. The
 * entity id, and the update form object sent from the client. Each
 * update controllers execute method will have a put mapping annotation.
 * Extends the [BaseRestController]
 */
abstract class BaseUpdateController<VIEWMODEL> : BaseRestController() {
    abstract fun execute(id: Long, model: VIEWMODEL): Result
}