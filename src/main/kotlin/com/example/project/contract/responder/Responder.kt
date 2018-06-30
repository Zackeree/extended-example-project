package com.example.project.contract.responder

/**
 * Generic response object that CRUD operations extend from
 */
interface Responder<in SUCCESS_CALLBACK_TYPE, in FAILURE_CALLBACK_TYPE> {
    /**
     * Generic Success Callback
     */
    fun onSuccess(t: SUCCESS_CALLBACK_TYPE)

    /**
     * Generic Failure Callback
     */
    fun onFailure(e: FAILURE_CALLBACK_TYPE)
}