package com.example.project.contract.person

import com.example.project.contract.BaseCRUDTest
import com.example.project.contract.responder.RetrieveResponder
import com.example.project.repository.person.IPersonRepository
import com.example.project.repository.person.Person
import com.example.project.repository.user.IUserRepository
import com.example.project.repository.user.User
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional

@DataJpaTest
@Transactional
@RunWith(SpringRunner::class)
class RetrieveTest : BaseCRUDTest() {

    @Autowired
    private lateinit var personRepo: IPersonRepository

    @Autowired
    private lateinit var userRepo: IUserRepository

    private var validId = -1L

    @Before
    fun init() {
        val user = userRepo.save(User("username", "email@address.com", "password123"))
        val person = Person("Cody", "Spath")
        person.user = user
        personRepo.save(person)
        validId = person.id
    }

    @Test
    fun retrieve_idNotFound_Failure() {
        var executed = false
        val responder = object : RetrieveResponder<PersonInfo> {
            override fun onSuccess(t: PersonInfo) {
                fail("Should not succeed")
            }

            override fun onFailure(e: String) {
                executed = true
            }
        }
        Retrieve(
                id = 666L,
                responder = responder,
                personRepo = personRepo
        ).execute()
        assertTrue(executed)
    }

    @Test
    fun retrieve_Success() {
        var executed = false
        val responder = object : RetrieveResponder<PersonInfo> {
            override fun onSuccess(t: PersonInfo) {
                executed = true
                assertEquals(validId, t.id)
            }

            override fun onFailure(e: String) {
                fail("Should not fail")
            }
        }
        Retrieve(
                id = validId,
                responder = responder,
                personRepo = personRepo
        ).execute()
        assertTrue(executed)
    }
}