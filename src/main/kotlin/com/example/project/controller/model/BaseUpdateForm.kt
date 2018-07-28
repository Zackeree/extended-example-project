package com.example.project.controller.model

interface BaseUpdateForm<REQUEST> {
    fun toRequest(id: Long?): REQUEST
}