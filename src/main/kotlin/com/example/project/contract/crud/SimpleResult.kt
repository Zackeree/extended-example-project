package com.example.project.contract.crud

data class SimpleResult<out S, out E>(val success: S?, val error: E?)