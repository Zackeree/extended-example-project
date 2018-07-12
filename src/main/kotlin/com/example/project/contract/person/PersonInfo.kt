package com.example.project.contract.person

import com.example.project.repository.person.Person

/**
 * View class for the [Person] entity
 */
data class PersonInfo constructor(
        val id: Long,
        val firstName: String,
        val lastName: String
) {
    constructor(person: Person) : this(
            id = person.id,
            firstName = person.firstName,
            lastName = person.lastName
    )

    companion object {
        fun infoList(persons: List<Person>): List<PersonInfo> {
            val infoList = mutableListOf<PersonInfo>()
            persons.forEach { person->
                infoList.add(PersonInfo(person))
            }

            return infoList
        }
    }
}