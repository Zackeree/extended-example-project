package com.example.project.controller.model

interface BaseCreateForm<REQUEST> {
    fun toCreateRequest(): REQUEST
}