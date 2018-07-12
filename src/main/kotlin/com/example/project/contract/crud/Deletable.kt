package com.example.project.contract.crud

import com.example.project.contract.Command
import com.example.project.contract.responder.DeleteResponder
import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap

interface Deletable<ERROR_TAG> {
    fun delete(id: Long, responder: DeleteResponder<ERROR_TAG>): Command

    fun delete(id: Long): SimpleDeleteResult<ERROR_TAG> {
        var result = SimpleDeleteResult<ERROR_TAG>(null, null)
        delete(
                id = id,
                responder = object : DeleteResponder<ERROR_TAG> {
                    override fun onSuccess(t: Long) {
                        result = SimpleDeleteResult(t, null)
                    }

                    override fun onFailure(e: HashMultimap<ERROR_TAG, String>) {
                        result = SimpleDeleteResult(null, e)
                    }
                }
        ).execute()
        return result
    }
}

data class SimpleDeleteResult<ERROR_TAG>(val success: Long?, val error: Multimap<ERROR_TAG, String>?)