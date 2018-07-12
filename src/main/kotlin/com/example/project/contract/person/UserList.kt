package com.example.project.contract.person

import com.example.project.contract.Command
import com.example.project.contract.responder.ListResponder
import com.example.project.repository.person.IPersonRepository
import com.example.project.repository.user.IUserRepository
import com.google.common.collect.HashMultimap

class UserList(
        private val userId: Long,
        private val responder: ListResponder<PersonInfo, ErrorTag>,
        private val userRepo: IUserRepository,
        private val personRepo: IPersonRepository
) : Command {
    override fun execute() {
        if (!userRepo.existsById(userId)) {
            val errors = HashMultimap.create<ErrorTag, String>()
            errors.put(ErrorTag.USER, "User#$userId not found")
            responder.onFailure(errors)
            return
        }
        val theUser = userRepo.findById(userId).get()
        responder.onSuccess(PersonInfo.infoList(personRepo.findByUserId(theUser.id)))
    }
}