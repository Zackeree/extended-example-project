package com.example.project.contract.person

import com.example.project.contract.Command
import com.example.project.contract.responder.DeleteResponder
import com.example.project.repository.person.IPersonRepository
import com.example.project.repository.person.Person
import com.google.common.collect.HashMultimap

/**
 * User Delete Command. Extends the [Command] object
 * @property id the [Person] id
 */
class Delete(
        private val id: Long,
        private val responder: DeleteResponder<ErrorTag>,
        private val personRepo: IPersonRepository
): Command {
    /**
     * Override the [Command] object execute method. If the
     * person id does not exist, respond with an error.
     * Otherwise, delete the person
     */
    override fun execute() {
        if (!personRepo.existsById(id)) {
            val errors = HashMultimap.create<ErrorTag, String>()
            errors.put(ErrorTag.ID, "Person#$id does not exist")
            responder.onFailure(errors)
            return
        }
        val thePerson = personRepo.findById(id).get()
        personRepo.delete(thePerson)
        responder.onSuccess(thePerson.id)
    }
}