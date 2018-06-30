package com.example.project.contract.person

import com.example.project.contract.Command
import com.example.project.contract.responder.*
import org.springframework.data.domain.Pageable

interface PersonFactory {
    fun retrieve(id: Long, responder: RetrieveResponder<PersonInfo>): Command
    fun create(request: Create.Request, responder: CreateResponder<ErrorTag>): Command
    fun update(request: Update.Request, responder: UpdateResponder<ErrorTag>): Command
    fun delete(id: Long, responder: DeleteResponder): Command
    fun findByFirstName(firstName: String, pageable: Pageable, responder: PageResponder<PersonInfo, ErrorTag>): Command
    fun findByLastName(lastName: String, pageable: Pageable, responder: PageResponder<PersonInfo, ErrorTag>): Command
}