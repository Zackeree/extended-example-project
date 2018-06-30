package com.example.project.contract.person

import com.example.project.repository.person.Person

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

}