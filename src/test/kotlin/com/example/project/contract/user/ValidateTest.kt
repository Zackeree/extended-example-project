package com.example.project.contract.user

import com.example.project.contract.responder.RetrieveResponder
import com.example.project.repository.user.IUserRepository
import com.example.project.repository.user.User
import com.google.common.collect.Multimap
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional

@DataJpaTest
@Transactional
@RunWith(SpringRunner::class)
class ValidateTest {

    @Autowired
    private lateinit var userRepo: IUserRepository

    private val passwordEncoder = BCryptPasswordEncoder()

    private val baseRequest = Validate.Request(
            usernameOrEmail = "cspath1@ycp.edu",
            password = "password"
    )

    private var id = -1L

    @Before
    fun init() {
        val userPassword = passwordEncoder.encode("password")
        val user = userRepo.save(User("username", "cspath1@ycp.edu", userPassword))
        id = user.id
    }

    @Test
    fun retrieveUserByEmailAndPassword_Success() {
        var executed = false
        val responder = object : RetrieveResponder<UserInfo, ErrorTag> {
            override fun onSuccess(t: UserInfo) {
                executed = true
                assertEquals(t.id, id)
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                fail("Should not fail")
            }
        }
        Validate(
                request = baseRequest,
                responder = responder,
                userRepo = userRepo
        ).execute()
        assertTrue(executed)
    }

    @Test
    fun retrieveUserByUsernameAndPassword_Success() {
        var executed = false
        val request = baseRequest.copy(usernameOrEmail = "username")
        val responder = object : RetrieveResponder<UserInfo, ErrorTag> {
            override fun onSuccess(t: UserInfo) {
                executed = true
                assertEquals(t.id, id)
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                fail("Should not fail")
            }
        }
        Validate(
                request = request,
                responder = responder,
                userRepo = userRepo
        ).execute()
        assertTrue(executed)
    }

    @Test
    fun blankUsernameOrEmail_Failure() {
        var executed = false
        val request = baseRequest.copy(usernameOrEmail = "")
        val responder = object : RetrieveResponder<UserInfo, ErrorTag> {
            override fun onSuccess(t: UserInfo) {
                fail("Should not succeed")
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                executed = true
            }
        }
        Validate(
                request = request,
                responder = responder,
                userRepo = userRepo
        ).execute()
        assertTrue(executed)
    }

    @Test
    fun wrongPassword_Failure() {
        var executed = false
        val request = baseRequest.copy(password = "passwrod")
        val responder = object : RetrieveResponder<UserInfo, ErrorTag> {
            override fun onSuccess(t: UserInfo) {
                fail("Should not succeed")
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                executed = true
            }
        }
        Validate(
                request = request,
                responder = responder,
                userRepo = userRepo
        ).execute()
        assertTrue(executed)
    }

}