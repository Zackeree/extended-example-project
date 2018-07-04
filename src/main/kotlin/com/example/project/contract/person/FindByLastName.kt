package com.example.project.contract.person

import com.example.project.contract.Command
import com.example.project.contract.responder.PageResponder
import com.example.project.repository.person.IPersonRepository
import com.example.project.repository.person.Person
import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

/**
 * Person find by last name command. Extends the [Command] object.
 * @property lastName the target [String]
 * @property pageable the [Pageable] object
 * @property responder the [PageResponder] interface
 * @property personRepo the [IPersonRepository] interface
 */
class FindByLastName(
        private val lastName: String,
        private val pageable: Pageable,
        private val responder: PageResponder<PersonInfo, ErrorTag>,
        private val personRepo: IPersonRepository
) : Command {
    /**
     * Override the [Command] object execute method. It calls the [validateRequest]
     * method which handles all constraint checking and validation. If the validation
     * passes, grab the [Page] of [Person] objects, adapt it to a page of [PersonInfo]
     * objects, and respond with it. Otherwise, respond with a list of the errors
     */
    override fun execute() {
        val errors = validateRequest()
        if (errors.isEmpty) {
            val personList = personRepo.findByLastName(lastName, pageable)
            responder.onSuccess(toPersonInfoList(personList = personList.content))
        } else {
            responder.onFailure(errors)
        }
    }

    /**
     * Method responsible for constraint checking and validations for the [Person]
     * find by last name method. it will ensure the last name is not blank and
     * is under the max length
     */
    private fun validateRequest(): Multimap<ErrorTag, String> {
        val errors = HashMultimap.create<ErrorTag, String>()
        if (lastName.isBlank())
            errors.put(ErrorTag.LAST_NAME, "Last Name may not be blank")
        if (lastName.length > 50)
            errors.put(ErrorTag.LAST_NAME, "Last Name must be under 50 characters")

        return errors
    }

    /**
     * Private method to adapt a list of [Person] object into a [Page] of [PersonInfo]
     * objects
     */
    private fun toPersonInfoList(personList: List<Person>): Page<PersonInfo> {
        val infoList = ArrayList<PersonInfo>()
        personList.forEach {
            infoList.add(PersonInfo(it))
        }

        return PageImpl<PersonInfo>(infoList)
    }
}