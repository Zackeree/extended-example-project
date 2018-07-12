package com.example.project.contract.user

import com.example.project.contract.Command
import com.example.project.contract.crud.*
import com.example.project.contract.person.PersonInfo
import com.example.project.contract.responder.CreateResponder
import com.example.project.contract.responder.DeleteResponder
import com.example.project.contract.responder.RetrieveResponder
import com.example.project.contract.responder.UpdateResponder
import com.example.project.repository.user.User

/**
 * Abstract factory wrapper to all user CRUD operations. Each factory interface needs to
 * have a method for each command object for a given entity. For the [User] entity, we have
 * the [Create], [Retrieve], [Update], [Delete], and [FindByUsernameOrEmailAndPassword] command objects
 */
interface UserFactory : Creatable<Create.Request, ErrorTag>,
                    RetrievableById<UserInfo, ErrorTag>,
                    RetrieveByRequest<FindByUsernameOrEmailAndPassword.Request, UserInfo, ErrorTag>,
                    Updatable<Update.Request, ErrorTag>,
                    Deletable<ErrorTag>{
    /**
     * Abstract [User] [Retrieve] method
     */
    override fun retrieve(id: Long, responder: RetrieveResponder<UserInfo, ErrorTag>): Command

    /**
     * Abstract [User] [FindByUsernameOrEmailAndPassword] method
     */
    override fun retrieve(request: FindByUsernameOrEmailAndPassword.Request, responder: RetrieveResponder<UserInfo, ErrorTag>): Command

    /**
     * Abstract [User] [Create] method
     */
    override fun create(request: Create.Request, responder: CreateResponder<ErrorTag>): Command

    /**
     * Abstract [User] [Update] method
     */
    override fun update(request: Update.Request, responder: UpdateResponder<ErrorTag>): Command

    /**
     * Abstract [User] [Delete] method
     */
    override fun delete(id: Long, responder: DeleteResponder<ErrorTag>): Command
}