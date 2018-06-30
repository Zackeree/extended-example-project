package com.example.project.contract.person

import com.example.project.contract.Command
import com.example.project.contract.responder.DeleteResponder
import com.example.project.repository.person.IPersonRepository

class Delete(
        private val id: Long,
        private val responder: DeleteResponder,
        private val personRepo: IPersonRepository
): Command {
    override fun execute() {
        if (!personRepo.existsById(id)) {
            responder.onFailure("Person#$id does not exist")
            return
        }
        val thePerson = personRepo.findById(id).get()
        personRepo.delete(thePerson)
        responder.onSuccess(thePerson.id)
    }
}