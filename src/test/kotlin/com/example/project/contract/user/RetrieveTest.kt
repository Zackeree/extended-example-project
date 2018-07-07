package com.example.project.contract.user

import com.example.project.contract.responder.RetrieveResponder
import com.example.project.repository.user.IUserRepository
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
class RetrieveTest {

    @Autowired
    private lateinit var userRepo: IUserRepository

    private var validId = -1L

    private val baseRequest = Create.Request(
            username = "username",
            email = "email@address.com",
            password = "password123",
            passwordConfirm = "password123"
    )

    @Before
    fun init() {
        validId = userRepo.save(baseRequest.toEntity()).id
    }

    @After
    fun tearDown() {
        userRepo.deleteAll()
    }

    @Test
    fun retrieve_idNotFound_Failure() {
        var executed = false
        val responder = object : RetrieveResponder<UserInfo> {
            override fun onSuccess(t: UserInfo) {
                fail("Should not work")
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                executed = true
            }
        }
        Retrieve(
                id = 666L,
                responder = responder,
                userRepo = userRepo
        ).execute()
        assertTrue(executed)
    }

    @Test
    fun retrieve_Success() {
        var executed = false
        val responder = object : RetrieveResponder<UserInfo> {
            override fun onSuccess(t: UserInfo) {
                executed = true
                assertEquals(validId, t.id)
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                fail("Should not fail")
            }
        }
        Retrieve(
                id = validId,
                responder = responder,
                userRepo = userRepo
        ).execute()
        assertTrue(executed)
    }
}