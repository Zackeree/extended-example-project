package com.example.project.contract

internal interface BaseCreateRequest<ENTITY> {
    fun toEntity(): ENTITY
}