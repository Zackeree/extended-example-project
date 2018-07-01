package com.example.project.contract

import com.example.project.repository.person.IPersonRepository
import com.example.project.repository.role.IUserRoleRepository
import com.example.project.repository.user.IUserRepository
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit4.SpringRunner

@DataJpaTest
@RunWith(SpringRunner::class)
abstract class BaseCRUDTest {

    @Autowired
    private lateinit var personRepo: IPersonRepository

    @Autowired
    private lateinit var userRepo: IUserRepository

    @Autowired
    private lateinit var userRoleRepo: IUserRoleRepository

    @Before
    fun setUp() {
        personRepo.deleteAll()
        userRoleRepo.deleteAll()
        userRepo.deleteAll()
    }

    @After
    fun tearDown() {
        personRepo.deleteAll()
        userRoleRepo.deleteAll()
        userRepo.deleteAll()
    }
}