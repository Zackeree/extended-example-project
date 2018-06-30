package com.example.project.contract

/**
 * Encapsulates business logic into Command Objects. All CRUD operations implement
 * from the base Command object. See [com.example.contract.user.Create] for an example
 */
interface Command {
    /**
     * Callback to be executed
     */
    fun execute()
}