package com.example.project.controller.model

interface BaseCreateRequest<REQUEST> {
    fun toCreateRequest(): REQUEST
}