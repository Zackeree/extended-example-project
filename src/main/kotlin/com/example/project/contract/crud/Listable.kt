package com.example.project.contract.crud

import com.example.project.contract.Command
import com.example.project.contract.responder.ListResponder
import com.google.common.collect.Multimap

interface Listable<REQUEST, INFO, ERROR_TAG> {
    fun list(request: REQUEST, responder: ListResponder<INFO, ERROR_TAG>): Command

    fun list(request: REQUEST): SimpleListResult<INFO, ERROR_TAG> {
        var result = SimpleListResult<INFO, ERROR_TAG>(null, null)
        list(
                request = request,
                responder = object : ListResponder<INFO, ERROR_TAG> {
                    override fun onSuccess(t: List<INFO>) {
                        result = SimpleListResult(t, null)
                    }

                    override fun onFailure(e: Multimap<ERROR_TAG, String>) {
                        result = SimpleListResult(null, e)
                    }
                }
        ).execute()
        return result
    }
}

data class SimpleListResult<INFO, ERROR_TAG>(val success: List<INFO>?, val error: Multimap<ERROR_TAG, String>?)