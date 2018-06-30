package com.example.project.contract.responder

/**
 * Abstract retrieve response object for Retrieve commands
 */
interface RetrieveResponder<in T> : Responder<T, String>