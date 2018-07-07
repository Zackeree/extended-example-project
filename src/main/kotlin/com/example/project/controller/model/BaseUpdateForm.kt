package com.example.project.controller.model

interface BaseUpdateForm<REQUEST> {
    fun toUpdateRequest(): REQUEST
}