package com.example.project.repository.person

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

/**
 * Spring ORM Repository that acts as an interface between the business logic and the database for the
 * user table. Extends the [PagingAndSortingRepository] interface which allows for easy integration
 * with returning paged data from the database. See [org.springframework.data.domain.Page] and
 * [org.springframework.data.domain.Pageable] for more information
 */
@Repository
@Qualifier(value = "personRepo")
interface IPersonRepository : PagingAndSortingRepository<Person, Long>, JpaSpecificationExecutor<Person> {
    /**
     * Query that will take a first name and a [Pageable] object
     * @param firstName the first name
     * @param pageable the [Pageable] object
     * @return a [Page] of [Person] objects
     */
    fun findByFirstName(firstName: String, pageable: Pageable): Page<Person>

    /**
     * Query that will take a last name and a [Pageable] object
     * @param lastName the last name
     * @param pageable the [Pageable] object
     * @return a [Page] of [Person] objects
     */
    fun findByLastName(lastName: String, pageable: Pageable): Page<Person>
}