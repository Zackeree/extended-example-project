package com.example.project.contract.crud

import com.example.project.contract.Command
import com.example.project.contract.responder.CreateResponder
import com.google.common.collect.Multimap

interface Creatable<REQUEST, ERROR_TAG> {
    fun create(request: REQUEST, responder: CreateResponder<ERROR_TAG>): Command

    fun create(request: REQUEST): SimpleCreateResult<ERROR_TAG> {
        var result = SimpleCreateResult<ERROR_TAG>(null, null)
        create(
                request = request,
                responder = object : CreateResponder<ERROR_TAG> {
                    override fun onSuccess(t: Long) {
                        result = SimpleCreateResult(t, null)
                    }

                    override fun onFailure(e: Multimap<ERROR_TAG, String>) {
                        result = SimpleCreateResult(null, e)
                    }
                }
        ).execute()
        return result
    }
}

data class SimpleCreateResult<T>(val success: Long?, val error: Multimap<T, String>?)