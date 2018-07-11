package com.example.project.repository.user

import com.example.project.repository.role.UserRole
import javax.persistence.*

/**
 * [Entity] class representing the SQL User table. Spring annotations allow
 * us to specify the table and column names, apply constraints (such as unique values,
 * non-null values only, etc) and builds out some of the ORM specifics, for instance,
 * one user to many people. See [Column], [GeneratedValue], [Entity],
 * [Table], [Id], and [JoinColumn] for some more information
 */
@Entity
@Table(name = "user")
data class User(
        @Column(name = "username", unique = true)
        var username: String,
        @Column(name = "email", unique = true)
        var email: String,
        @Column(name = "password")
        var password: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    var id: Long = 0

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    var roles: List<UserRole> = arrayListOf()

    constructor(id: Long, username: String, email: String, password: String) : this(username, email, password) {
        this.id = id
    }
}
