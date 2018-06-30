package com.example.project.contract

interface BaseUpdateRequest<ENTITY> {
    fun createUpdatedEntity(e: ENTITY): ENTITY
}