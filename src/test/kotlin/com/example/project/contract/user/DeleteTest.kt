package com.example.project.contract.user

import com.example.project.contract.responder.DeleteResponder
import com.example.project.repository.user.IUserRepository
import com.google.common.collect.HashMultimap
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
class DeleteTest {

    @Autowired
    private lateinit var userRepo: IUserRepository

    private val baseCreateRequest = Create.Request(
            username = "username",
            email = "email@address.com",
            password = "password1234",
            passwordConfirm = "password1234"
    )

    private var existingId = -1L

    @Before
    fun init() {
        val user = userRepo.save(baseCreateRequest.toEntity())
        existingId = user.id
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
                userRepo = userRepo
        ).execute()
        assertEquals(userRepo.count(), 1)
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
        Delete(id = existingId,
                responder = responder,
                userRepo = userRepo
        ).execute()
        assertEquals(userRepo.count(), 0)
        assertTrue(executed)
    }

}