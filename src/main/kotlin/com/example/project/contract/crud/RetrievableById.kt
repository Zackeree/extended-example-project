package com.example.project.contract.crud

import com.example.project.contract.Command
import com.example.project.contract.responder.RetrieveResponder
import com.google.common.collect.Multimap

interface RetrievableById<INFO, ERROR_TAG> {
    fun retrieve(id: Long, responder: RetrieveResponder<INFO, ERROR_TAG>): Command

    fun retrieve(id: Long): SimpleRetrieveResult<INFO, ERROR_TAG> {
        var result = SimpleRetrieveResult<INFO, ERROR_TAG>(null, null)
        retrieve(
                id = id,
                responder = object : RetrieveResponder<INFO, ERROR_TAG> {
                    override fun onSuccess(t: INFO) {
                        result = SimpleRetrieveResult(t, null)
                    }

                    override fun onFailure(e: Multimap<ERROR_TAG, String>) {
                        result = SimpleRetrieveResult(null, e)
                    }
                }
        ).execute()
        return result
    }
}

data class SimpleRetrieveResult<INFO, ERROR_TAG>(val success: INFO?, val error: Multimap<ERROR_TAG, String>?)