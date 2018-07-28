package com.example.project.contract

/**
 * Interface for Update Request objects
 */
interface BaseUpdateRequest<ENTITY> {
    fun createUpdatedEntity(e: ENTITY): ENTITY
}