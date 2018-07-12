package com.example.project.contract.crud

import com.example.project.contract.Command
import com.example.project.contract.responder.RetrieveResponder
import com.google.common.collect.Multimap

interface RetrieveByRequest<REQUEST, INFO, ERROR_TAG> {
    fun retrieve(request: REQUEST, responder : RetrieveResponder<INFO, ERROR_TAG>): Command

    fun retrieve(request: REQUEST): SimpleRetrieveRyRequestResult<INFO, ERROR_TAG> {
        var result = SimpleRetrieveRyRequestResult<INFO, ERROR_TAG>(null, null)
        retrieve(
                request = request,
                responder = object : RetrieveResponder<INFO, ERROR_TAG> {
                    override fun onSuccess(t: INFO) {
                        result = SimpleRetrieveRyRequestResult(t, null)
                    }

                    override fun onFailure(e: Multimap<ERROR_TAG, String>) {
                        result = SimpleRetrieveRyRequestResult(null, e)
                    }
                }
        ).execute()
        return result
    }
}

data class SimpleRetrieveRyRequestResult<INFO, ERROR_TAG>(val success: INFO?, val error: Multimap<ERROR_TAG, String>?)