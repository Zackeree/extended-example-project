package com.example.project.controller.model

interface BaseForm<REQUEST> {
    fun toRequest(): REQUEST
}