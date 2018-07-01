package com.example.project.repository.role

import com.example.project.repository.user.User
import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
@Table(name = "user_role")
data class UserRole(
        @Column(name = "role")
        var role: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    var id: Long = 0

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    var user: User? = null

    constructor(user: User, role: String) : this(role) {
        this.user = user
    }
}