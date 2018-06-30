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

class FindByFirstName(
        private val firstName: String,
        private val pageable: Pageable,
        private val responder: PageResponder<PersonInfo, ErrorTag>,
        private val personRepo: IPersonRepository
) : Command {
    override fun execute() {
        val errors = validateRequest()
        if (errors.isEmpty) {
            val personList = personRepo.findByFirstName(firstName, pageable)
            val infoPage = toPersonInfoList(personList = personList.content)
            responder.onSuccess(infoPage)
        } else {
            responder.onFailure(errors)
        }
    }

    private fun validateRequest(): Multimap<ErrorTag, String> {
        val errors = HashMultimap.create<ErrorTag, String>()
        if (firstName.isBlank())
            errors.put(ErrorTag.FIRST_NAME, "First Name may not be blank")
        if (firstName.length > 50)
            errors.put(ErrorTag.FIRST_NAME, "First Name must be under 50 characters")

        return errors
    }

    private fun toPersonInfoList(personList: List<Person>): Page<PersonInfo> {
        val infoList = ArrayList<PersonInfo>()
        personList.forEach {
            infoList.add(PersonInfo(it))
        }

        return PageImpl<PersonInfo>(infoList)
    }
}