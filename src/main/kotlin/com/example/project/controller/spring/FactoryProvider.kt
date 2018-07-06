package com.example.project.controller.spring

import com.example.project.contract.person.UserPersonWrapper
import com.example.project.contract.user.UserUserWrapper

interface FactoryProvider {
    fun getUserWrapper(): UserUserWrapper
    fun getPersonWrapper(): UserPersonWrapper
}