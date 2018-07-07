package com.example.project.contract.person

import com.example.project.contract.BaseCRUDTest
import com.example.project.contract.responder.DeleteResponder
import com.example.project.repository.person.IPersonRepository
import com.example.project.repository.person.Person
import com.example.project.repository.user.IUserRepository
import com.example.project.repository.user.User
import com.google.common.collect.HashMultimap
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional

@DataJpaTest
@Transactional
@RunWith(SpringRunner::class)
class DeleteTest : BaseCRUDTest() {

    @Autowired
    private lateinit var userRepo: IUserRepository

    @Autowired
    private lateinit var personRepo: IPersonRepository

    private var existingId = -1L

    @Before
    fun init() {
        val user = userRepo.save(User("username", "email@address.com", "password123"))
        val person = Person("Cody", "Spath")
        person.user = user
        personRepo.save(person)
        existingId = person.id
    }

    @Test
    fun idDoesNotExist_Failure() {
        var executed = false
        val responder = object : DeleteResponder<ErrorTag> {
            override fun onSuccess(t: Long) {
                fail("Should not succeed")
            }

            override fun onFailure(e: HashMultimap<ErrorTag, String>) {
                executed = true
            }
        }
        Delete(
                id = 666L,
                responder = responder,
                personRepo = personRepo
        ).execute()
        assertEquals(personRepo.count(), 1)
        assertTrue(executed)
    }

    @Test
    fun delete_Success() {
        var executed = false
        val responder = object : DeleteResponder<ErrorTag> {
            override fun onSuccess(t: Long) {
                executed = true
                assertEquals(existingId, t)
            }

            override fun onFailure(e: HashMultimap<ErrorTag, String>) {
                fail("Should not fail")
            }
        }
        Delete(
                id = existingId,
                responder = responder,
                personRepo = personRepo
        ).execute()
        assertEquals(personRepo.count(), 0)
        assertTrue(executed)
    }

}