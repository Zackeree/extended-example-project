package com.example.project.contract

import com.example.project.contract.crud.SimpleResult
import com.example.project.controller.security.AccessReport

interface Executable<out S, out E> {
  fun execute(): SimpleResult<S, E>
}

interface SecuredAction<out S, out E> {
  fun execute(withAccess: (result: SimpleResult<S, E>)->Unit): AccessReport?
}