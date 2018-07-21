package com.example.project.controller.security.crud

import com.example.project.controller.security.AccessReport

interface UserCreatable<in REQUEST, out RESULT> {
  fun create(request: REQUEST, withAccess: (result: RESULT)->Unit): AccessReport?
}