package com.example.project.contract

/**
 * Interface for Create Request objects
 */
internal interface BaseCreateRequest<ENTITY> {
    fun toEntity(): ENTITY
}