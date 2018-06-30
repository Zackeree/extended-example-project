package com.example.project.contract.person

import com.example.project.contract.Command
import com.example.project.contract.responder.RetrieveResponder
import com.example.project.repository.person.IPersonRepository

class Retrieve(
        private val id: Long,
        private val responder: RetrieveResponder<PersonInfo>,
        private val personRepo: IPersonRepository
) : Command {
    override fun execute() {
        if (!personRepo.existsById(id)) {
            responder.onFailure("Person#$id not found")
            return
        }
        val thePerson = personRepo.findById(id)
        val theInfo = PersonInfo(person = thePerson.get())
        responder.onSuccess(theInfo)
    }
}