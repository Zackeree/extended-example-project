package com.example.project.contract.person

import com.example.project.contract.responder.CreateResponder
import com.example.project.contract.responder.DeleteResponder
import com.example.project.contract.responder.RetrieveResponder
import com.example.project.contract.responder.UpdateResponder
import com.example.project.repository.person.IPersonRepository
import com.example.project.repository.user.IUserRepository
import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional

@DataJpaTest
@Transactional
@RunWith(SpringRunner::class)
class BasePersonFactoryTest {

    @Autowired
    private lateinit var userRepo: IUserRepository

    @Autowired
    private lateinit var personRepo: IPersonRepository

    @Test
    fun retrieve() {
        val factory = BasePersonFactory(personRepo, userRepo)
        val cmd = factory.retrieve(
                id = 0,
                responder = object : RetrieveResponder<PersonInfo> {
                    override fun onSuccess(t: PersonInfo) { }
                    override fun onFailure(e: Multimap<com.example.project.contract.user.ErrorTag, String>) { }
                }
        )
        assertTrue(cmd is Retrieve)
    }

    @Test
    fun create() {
        val factory = BasePersonFactory(personRepo, userRepo)
        val cmd = factory.create(
                request = Create.Request(
                        userId = 0,
                        firstName = "Cody",
                        lastName = "Spath"
                ),
                responder = object : CreateResponder<ErrorTag> {
                    override fun onSuccess(t: Long) { }
                    override fun onFailure(e: Multimap<ErrorTag, String>) { }
                }
        )
        assertTrue(cmd is Create)
    }

    @Test
    fun update() {
        val factory = BasePersonFactory(personRepo, userRepo)
        val cmd = factory.update(
                request = Update.Request(
                        id = 0,
                        firstName = "Thom",
                        lastName = "Yorke"
                ),
                responder = object : UpdateResponder<ErrorTag> {
                    override fun onSuccess(t: Long) { }
                    override fun onFailure(e: Multimap<ErrorTag, String>) { }
                }
        )
        assertTrue(cmd is Update)
    }

    @Test
    fun delete() {
        val factory = BasePersonFactory(personRepo, userRepo)
        val cmd = factory.delete(
                id = 0,
                responder = object : DeleteResponder<ErrorTag> {
                    override fun onSuccess(t: Long) { }
                    override fun onFailure(e: HashMultimap<ErrorTag, String>) { }
                }
        )
        assertTrue(cmd is Delete)
    }
}