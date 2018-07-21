package com.example.project.contract.crud

import com.example.project.contract.Command
import com.example.project.contract.responder.CreateResponder
import com.google.common.collect.Multimap

interface Creatable<in REQUEST, ERROR_TAG> {
    fun create(request: REQUEST, responder: CreateResponder<ERROR_TAG>): Command

    fun create(request: REQUEST): SimpleResult<Long, Multimap<ERROR_TAG, String>> {
        var result = SimpleResult<Long, Multimap<ERROR_TAG, String>>(null, null)
        create(
                request = request,
                responder = object : CreateResponder<ERROR_TAG> {
                    override fun onSuccess(t: Long) {
                        result = SimpleResult(t, null)
                    }

                    override fun onFailure(e: Multimap<ERROR_TAG, String>) {
                        result = SimpleResult(null, e)
                    }
                }
        ).execute()
        return result
    }
}