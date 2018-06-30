package com.example.project.contract.user

import com.example.project.contract.Command
import com.example.project.contract.crud.Creatable
import com.example.project.contract.responder.CreateResponder
import com.example.project.contract.responder.DeleteResponder
import com.example.project.contract.responder.RetrieveResponder
import com.example.project.contract.responder.UpdateResponder

/**
 * Abstract factory wrapper to all user CRUD operations
 */
interface UserFactory {
    /**
     * Abstract User [Retrieve] method
     */
    fun retrieve(id: Long, responder: RetrieveResponder<UserInfo>): Command

    /**
     * Abstract User [FindByUsernameOrEmail] methdod
     */
    fun retrieve(username: String, responder: RetrieveResponder<UserInfo>): Command

    /**
     * Abstract User [Create] method
     */
    fun create(request: Create.Request, responder: CreateResponder<ErrorTag>): Command

    /**
     * Abstract User [Update] method
     */
    fun update(request: Update.Request, responder: UpdateResponder<ErrorTag>): Command

    /**
     * Abstract User [Delete] method
     */
    fun delete(id: Long, responder: DeleteResponder): Command
}