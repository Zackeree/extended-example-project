package com.example.project.repository.person

import au.com.console.jpaspecificationdsl.equal
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
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
interface IPersonRepository : PagingAndSortingRepository<Person, Long>, JpaSpecificationExecutor<Person> {
    /**
     * Query that will take a first name and a [Pageable] object
     * @param firstName the first name
     * @param pageable the [Pageable] object
     * @return a [Page] of [Person] objects
     */
    fun findByFirstName(firstName: String, pageable: Pageable): Page<Person> {
        val people = findAll(Person::firstName.equal(firstName))
        return PageImpl<Person>(people, pageable, people.size.toLong())
    }

    /**
     * Query that will take a last name and a [Pageable] object
     * @param lastName the last name
     * @param pageable the [Pageable] object
     * @return a [Page] of [Person] objects
     */
    fun findByLastName(lastName: String, pageable: Pageable): Page<Person>
}