package com.example.project.contract.person

import com.example.project.contract.Command
import com.example.project.contract.responder.RetrieveResponder
import com.example.project.contract.user.ErrorTag
import com.example.project.repository.person.IPersonRepository
import com.google.common.collect.HashMultimap
import org.springframework.stereotype.Service

/**
 * Person Retrieve command. Extends the [Command] object.
 * @property id the person id
 * @property responder the [RetrieveResponder] interface
 * @property personRepo the [IPersonRepository] interface
 */
class Retrieve(
        private val id: Long,
        private val responder: RetrieveResponder<PersonInfo>,
        private val personRepo: IPersonRepository
) : Command {
    /**
     * Override of the [Command] object execute method.
     * If the person does not exist, respond with an error.
     * Otherwise, retrieve the user and adapt it to a
     * [PersonInfo] and respond with it
     */
    override fun execute() {
        if (!personRepo.existsById(id)) {
            val errors = HashMultimap.create<ErrorTag, String>()
            errors.put(ErrorTag.ID, "Person#$id not found")
            responder.onFailure(errors)
            return
        }
        val thePerson = personRepo.findById(id)
        val theInfo = PersonInfo(person = thePerson.get())
        responder.onSuccess(theInfo)
    }
}