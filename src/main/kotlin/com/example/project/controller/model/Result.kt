package com.example.project.controller.model

import org.springframework.http.HttpStatus

class Result(
        var data: Any?=null,
        var errors: Map<String, Collection<String>>?=null
) {
    constructor(data: Any?, errors: Map<String, Collection<String>>?, status: HttpStatus) : this(data, errors){
        this.status = status
    }
    var status: HttpStatus = if (errors == null) HttpStatus.OK
    else HttpStatus.BAD_REQUEST
}