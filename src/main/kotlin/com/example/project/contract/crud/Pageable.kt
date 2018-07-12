package com.example.project.contract.crud

import com.example.project.contract.Command
import com.example.project.contract.responder.PageResponder
import com.google.common.collect.Multimap
import org.springframework.data.domain.Page

interface Pageable<REQUEST, INFO, ERROR_TAG> {
    fun page(request: REQUEST, responder: PageResponder<INFO, ERROR_TAG>): Command

    fun page(request: REQUEST): SimplePageResult<INFO, ERROR_TAG> {
        var result = SimplePageResult<INFO, ERROR_TAG>(null, null)
        page(
                request = request,
                responder = object : PageResponder<INFO, ERROR_TAG> {
                    override fun onSuccess(t: Page<INFO>) {
                        result = SimplePageResult(t, null)
                    }

                    override fun onFailure(e: Multimap<ERROR_TAG, String>) {
                        result = SimplePageResult(null, e)
                    }
                }
        ).execute()
        return result
    }
}

data class SimplePageResult<INFO, ERROR_TAG>(val success: Page<INFO>?, val error: Multimap<ERROR_TAG, String>?)