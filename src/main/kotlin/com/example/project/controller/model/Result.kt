package com.example.project.controller.model

import org.springframework.http.HttpStatus

class Result(
        var data: Any?=null,
        var errors: Map<String, Collection<String>>?=null
) {
    var status: HttpStatus = if (errors == null) HttpStatus.OK
    else HttpStatus.BAD_REQUEST
}