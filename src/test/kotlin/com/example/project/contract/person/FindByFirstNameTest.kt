package com.example.project.contract.person

import com.example.project.contract.BaseCRUDTest
import com.example.project.contract.responder.PageResponder
import com.example.project.repository.person.IPersonRepository
import com.example.project.repository.person.Person
import com.example.project.repository.user.IUserRepository
import com.example.project.repository.user.User
import com.google.common.collect.Multimap
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional

@DataJpaTest
@Transactional
@RunWith(SpringRunner::class)
class FindByFirstNameTest : BaseCRUDTest() {

    @Autowired
    private lateinit var userRepo: IUserRepository

    @Autowired
    private lateinit var personRepo: IPersonRepository

    private val user = User("username", "email@address.com", "password123")

    @Before
    fun init() {
        userRepo.save(user)

        val person1 = Person("Cody", "Spath")
        person1.user = user
        personRepo.save(person1)

        val person2 = Person("Cody", "Spat")
        person2.user = user
        personRepo.save(person2)
    }

    @Test
    fun testValidConstraints_Success() {
        var executed = false
        val firstName = "Cody"
        val pageable = PageRequest.of(0, 5)
        val responder = object : PageResponder<PersonInfo, ErrorTag> {
            override fun onSuccess(t: Page<PersonInfo>) {
                executed = true
                assertTrue(t.content.size == 2)
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                fail("Should not fail")
            }
        }
        FindByFirstName(
                firstName = firstName,
                pageable = pageable,
                responder = responder,
                personRepo = personRepo
        ).execute()
        assertTrue(executed)
    }

    @Test
    fun testBlankFirstName_Failure() {
        var executed = false
        val firstName = ""
        val pageable = PageRequest.of(0,5)
        val responder = object : PageResponder<PersonInfo, ErrorTag> {
            override fun onSuccess(t: Page<PersonInfo>) {
                fail("Should not succeed")
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                executed = true
                assertTrue(e[ErrorTag.FIRST_NAME].isNotEmpty())
            }
        }
        FindByFirstName(
                firstName = firstName,
                pageable = pageable,
                responder = responder,
                personRepo = personRepo
        ).execute()
        assertTrue(executed)
    }

    @Test
    fun testFirstNameTooLong_Failure() {
        var executed = false
        val firstName = "hello".repeat(20)
        val pageable = PageRequest.of(0, 5)
        val responder = object : PageResponder<PersonInfo, ErrorTag> {
            override fun onSuccess(t: Page<PersonInfo>) {
                fail("Should not succeed")
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                executed = true
                assertTrue(e[ErrorTag.FIRST_NAME].isNotEmpty())
            }
        }
        FindByFirstName(
                firstName = firstName,
                pageable = pageable,
                responder = responder,
                personRepo = personRepo
        ).execute()
        assertTrue(executed)
    }
}