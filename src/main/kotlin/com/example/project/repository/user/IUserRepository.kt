package com.example.project.repository.user

import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

/**
 * Spring ORM Repository that acts as an interface between the business logic and the database's
 * user table. Extends the [PagingAndSortingRepository] interface which allows for easy integration
 * with returning paged data from the database. See [org.springframework.data.domain.Page] and
 * [org.springframework.data.domain.Pageable] for more information
 */
@Repository
interface IUserRepository : PagingAndSortingRepository<User, Long>, JpaSpecificationExecutor<User> {

    /**
     * Query that will return a [User] object based on the username
     *
     * @param email the email
     * @return a [User]
     */
    fun findByUsername(username: String): User?

    fun findByEmail(email: String): User?

    @Query(value = "SELECT u FROM User u WHERE u.email=?1 or u.username=?1")
    fun findByUsernameOrEmail(target: String): User?

    fun countByUsername(username: String): Int

    fun countByEmail(email: String): Int
}
