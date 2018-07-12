package com.example.project.contract.crud

import com.example.project.contract.Command
import com.example.project.contract.responder.UpdateResponder
import com.google.common.collect.Multimap

interface Updatable<REQUEST, ERROR_TAG> {
    fun update(request: REQUEST, responder: UpdateResponder<ERROR_TAG>): Command

    fun update(request: REQUEST): SimpleUpdateResult<ERROR_TAG> {
        var result = SimpleUpdateResult<ERROR_TAG>(null, null)
        update(
                request = request,
                responder = object : UpdateResponder<ERROR_TAG> {
                    override fun onSuccess(t: Long) {
                        result = SimpleUpdateResult(t, null)
                    }

                    override fun onFailure(e: Multimap<ERROR_TAG, String>) {
                        result = SimpleUpdateResult(null, e)
                    }
                }
        ).execute()
        return result
    }
}

data class SimpleUpdateResult<T>(val success: Long?, val error: Multimap<T, String>?)