package com.example.project.contract.person

import com.example.project.contract.BaseCRUDTest
import com.example.project.contract.responder.UpdateResponder
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
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional

@DataJpaTest
@Transactional
@RunWith(SpringRunner::class)
class UpdateTest : BaseCRUDTest() {

    @Autowired
    private lateinit var userRepo: IUserRepository

    @Autowired
    private lateinit var personRepo: IPersonRepository

    private var baseRequest = Update.Request(
            id = -1L,
            firstName = "Thom",
            lastName = "Yorke"
    )

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
    fun idNotFound_Failure() {
        var executed = false
        buildUpdateCommand(
                request = baseRequest,
                onSuccess = {
                    fail("Should not happen")
                },
                onFailure = {
                    executed = true
                    assertTrue((it[ErrorTag.ID].isNotEmpty()))
                }
        ).execute()
        assertTrue(executed)
    }

    @Test
    fun blankFirstName_Failure() {
        var executed = false
        buildUpdateCommand(
                request = baseRequest.copy(
                        id = existingId,
                        firstName = ""
                ),
                onSuccess = {
                    fail("Should not succeed")
                },
                onFailure = {
                    executed = true
                    assertTrue(it[ErrorTag.FIRST_NAME].isNotEmpty())
                }
        ).execute()
        assertTrue(executed)
    }

    @Test
    fun firstNameTooLong_Failure() {
        var executed = false
        val newFirstName = "Thom".repeat(15)
        buildUpdateCommand(
                request = baseRequest.copy(
                        id = existingId,
                        firstName = newFirstName
                ),
                onSuccess = {
                    fail("Should not succeed")
                },
                onFailure = {
                    executed = true
                    assertTrue(it[ErrorTag.FIRST_NAME].isNotEmpty())
                }
        ).execute()
        assertTrue(executed)
    }

    @Test
    fun lastNameBlank_Failure() {
        var executed = false
        buildUpdateCommand(
                request = baseRequest.copy(
                        id = existingId,
                        lastName = ""
                ),
                onSuccess = {
                    fail("Should not succeed")
                },
                onFailure = {
                    executed = true
                    assertTrue(it[ErrorTag.LAST_NAME].isNotEmpty())
                }
        ).execute()
        assertTrue(executed)
    }

    @Test
    fun lastNameTooLong_Failure() {
        var executed = false
        val newLastName = "Yorke".repeat(15)
        buildUpdateCommand(
                request = baseRequest.copy(
                        id = existingId,
                        lastName = newLastName
                ),
                onSuccess = {
                    fail("Should not succeed")
                },
                onFailure = {
                    executed = true
                    assertTrue(it[ErrorTag.LAST_NAME].isNotEmpty())
                }
        ).execute()
        assertTrue(executed)
    }

    private fun buildUpdateCommand(request: Update.Request, onSuccess: (id: Long)->Unit, onFailure: (Multimap<ErrorTag, String>)->Unit): Update {
        val responder = object : UpdateResponder<ErrorTag> {
            override fun onSuccess(t: Long) {
                onSuccess(t)
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                onFailure(e)
            }
        }

        return Update(request, responder, personRepo)
    }
}