package com.example.project.controller.model

/**
 * Interface that all forms inherit from
 */
interface BaseCreateForm<REQUEST> {
    /**
     * Used to adapt form into contracts request
     */
    fun toRequest(): REQUEST
}