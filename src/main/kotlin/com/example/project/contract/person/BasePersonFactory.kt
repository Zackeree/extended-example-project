package com.example.project.contract.person

import com.example.project.contract.Command
import com.example.project.contract.responder.*
import com.example.project.repository.person.IPersonRepository
import com.example.project.repository.user.IUserRepository
import org.springframework.data.domain.Pageable

/**
 * Base concrete implementation of the [PersonFactory] interface
 */
class BasePersonFactory(
        private val personRepo: IPersonRepository,
        private val userRepo: IUserRepository
) : PersonFactory {
    /**
     * Override of the [PersonFactory.create] method that will return a [Create] command object
     */
    override fun create(request: Create.Request, responder: CreateResponder<ErrorTag>): Command {
        return Create(request, responder, personRepo, userRepo)
    }

    /**
     * Override of the [PersonFactory.retrieve] method that will return a [Retrieve] command object
     */
    override fun retrieve(id: Long, responder: RetrieveResponder<PersonInfo, ErrorTag>): Command {
        return Retrieve(id, responder, personRepo)
    }

    /**
     * Override of the [PersonFactory.update] method that will return a [Update] command object
     */
    override fun update(request: Update.Request, responder: UpdateResponder<ErrorTag>): Command {
        return Update(request, responder, personRepo)
    }

    /**
     * Override of the [PersonFactory.delete] method that will return a [Delete] command object
     */
    override fun delete(id: Long, responder: DeleteResponder<ErrorTag>): Command {
        return Delete(id, responder, personRepo)
    }

    /**
     * Override of the [PersonFactory.list] method that will return a [UserList] command object
     */
    override fun list(userId: Long, responder: ListResponder<PersonInfo, ErrorTag>): Command {
        return UserList(userId, responder, userRepo, personRepo)
    }

    /**
     * Override of the [PersonFactory.findByFirstName] method that will return a [FindByFirstName] command object
     */
    override fun findByFirstName(firstName: String, pageable: Pageable, responder: PageResponder<PersonInfo, ErrorTag>): Command {
        return FindByFirstName(firstName, pageable, responder, personRepo)
    }

    /**
     * Override of the [PersonFactory.findByLastName] method that will return a [FindByLastName] command object
     */
    override fun findByLastName(lastName: String, pageable: Pageable, responder: PageResponder<PersonInfo, ErrorTag>): Command {
        return FindByLastName(lastName, pageable, responder, personRepo)
    }

}