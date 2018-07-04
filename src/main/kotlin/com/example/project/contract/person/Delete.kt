package com.example.project.contract.person

import com.example.project.contract.Command
import com.example.project.contract.responder.DeleteResponder
import com.example.project.repository.person.IPersonRepository
import com.example.project.repository.person.Person

/**
 * User Delete Command. Extends the [Command] object
 * @property id the [Person] id
 */
class Delete(
        private val id: Long,
        private val responder: DeleteResponder,
        private val personRepo: IPersonRepository
): Command {
    /**
     * Override the [Command] object execute method. If the
     * person id does not exist, respond with an error.
     * Otherwise, delete the person
     */
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