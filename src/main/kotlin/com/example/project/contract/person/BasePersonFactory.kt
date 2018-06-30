package com.example.project.contract.person

import com.example.project.contract.Command
import com.example.project.contract.responder.*
import com.example.project.repository.person.IPersonRepository
import com.example.project.repository.user.IUserRepository
import org.springframework.data.domain.Pageable

class BasePersonFactory(
        private val personRepo: IPersonRepository,
        private val userRepo: IUserRepository
) : PersonFactory {
    override fun create(request: Create.Request, responder: CreateResponder<ErrorTag>): Command {
        return Create(request, responder, personRepo, userRepo)
    }

    override fun retrieve(id: Long, responder: RetrieveResponder<PersonInfo>): Command {
        return Retrieve(id, responder, personRepo)
    }

    override fun update(request: Update.Request, responder: UpdateResponder<ErrorTag>): Command {
        return Update(request, responder, personRepo)
    }

    override fun delete(id: Long, responder: DeleteResponder): Command {
        return Delete(id, responder, personRepo)
    }

    override fun findByFirstName(firstName: String, pageable: Pageable, responder: PageResponder<PersonInfo, ErrorTag>): Command {
        return FindByFirstName(firstName, pageable, responder, personRepo)
    }

    override fun findByLastName(lastName: String, pageable: Pageable, responder: PageResponder<PersonInfo, ErrorTag>): Command {
        return FindByLastName(lastName, pageable, responder, personRepo)
    }

}