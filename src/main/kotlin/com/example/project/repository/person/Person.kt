package com.example.project.repository.person

import com.example.project.repository.user.User
import com.fasterxml.jackson.annotation.JsonIgnore

import javax.persistence.*

/**
 * [Entity] class representing the SQL Person table. Spring annotations allow
 * us to specify the table and column names, apply constraints (such as unique values,
 * non-null values only, etc) and builds out some of the ORM specifics, for instance,
 * many people to one user. See [Column], [GeneratedValue], [Entity],
 * [Table], [Id], and [JoinColumn] for some more information
 */
@Entity
@Table(name = "person")
data class Person(
        @Column(name = "first_name")
        var firstName: String = "",
        @Column(name = "last_name")
        var lastName: String = ""
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    var id: Long = 0

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    var user: User? = null

    constructor(firstName: String, lastName: String, user: User) : this (firstName, lastName) {
        this.user = user
    }
}