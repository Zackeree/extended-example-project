package com.example.project.contract.user

import com.example.project.contract.Command
import com.example.project.contract.responder.CreateResponder
import com.example.project.contract.responder.DeleteResponder
import com.example.project.contract.responder.RetrieveResponder
import com.example.project.contract.responder.UpdateResponder
import com.example.project.repository.role.IUserRoleRepository
import com.example.project.repository.user.IUserRepository

/**
 * Base concrete implementation of the [UserFactory] interface
 */
class BaseUserFactory(
        private val userRepo: IUserRepository,
        private val userRoleRepo: IUserRoleRepository
) : UserFactory {
    /**
     * Override of the [UserFactory.retrieve] method that will return a [Retrieve] command object
     */
    override fun retrieve(id: Long, responder: RetrieveResponder<UserInfo, ErrorTag>): Command {
        return Retrieve(id, responder, userRepo)
    }

    /**
     * Override of the [UserFactory.retrieve] method that will return a [Validate] command object
     */
    override fun retrieve(request: Validate.Request, responder: RetrieveResponder<UserInfo, ErrorTag>): Command {
        return Validate(request, responder, userRepo)
    }

    /**
     * Override of the [UserFactory.create] method that will return a [Create] command object
     */
    override fun create(request: Create.Request, responder: CreateResponder<ErrorTag>): Command {
        return Create(request, responder, userRepo, userRoleRepo)
    }

    /**
     * Override of the [UserFactory.update] method that will return a [Update] command object
     */
    override fun update(request: Update.Request, responder: UpdateResponder<ErrorTag>): Command {
        return Update(request, responder, userRepo)
    }

    /**
     * Override of the [UserFactory.delete] method that will return a [Delete] command object
     */
    override fun delete(id: Long, responder: DeleteResponder<ErrorTag>): Command {
        return Delete(id, responder, userRepo)
    }
}