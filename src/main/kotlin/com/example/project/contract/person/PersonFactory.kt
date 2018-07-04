package com.example.project.contract.person

import com.example.project.repository.person.Person
import com.example.project.contract.Command
import com.example.project.contract.responder.*
import org.springframework.data.domain.Pageable

/**
 * Abstract factory wrapper to all user CRUD operations. Each factory interface needs to have a method for each command object for a given entity. For the [Person] entity, we have
 * the [Create], [Retrieve], [Update], [Delete], [FindByFirstName], and [FindByLastName] command objects
 */
interface PersonFactory {
    /**
     * Abstract [Person] [Retrieve] method
     */
    fun retrieve(id: Long, responder: RetrieveResponder<PersonInfo>): Command

    /**
     * Abstract [Person] [Create] method
     */
    fun create(request: Create.Request, responder: CreateResponder<ErrorTag>): Command

    /**
     * Abstract [Person] [Update] method
     */
    fun update(request: Update.Request, responder: UpdateResponder<ErrorTag>): Command

    /**
     * Abstract [Person] [Delete] method
     */
    fun delete(id: Long, responder: DeleteResponder): Command

    /**
     * Abstract [Person] [FindByFirstName] method
     */
    fun findByFirstName(firstName: String, pageable: Pageable, responder: PageResponder<PersonInfo, ErrorTag>): Command

    /**
     * Abstract [Person] [FindByLastName] method
     */
    fun findByLastName(lastName: String, pageable: Pageable, responder: PageResponder<PersonInfo, ErrorTag>): Command
}