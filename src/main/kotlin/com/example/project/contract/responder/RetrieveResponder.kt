package com.example.project.contract.responder

import com.example.project.contract.user.ErrorTag
import com.google.common.collect.Multimap

/**
 * Abstract retrieve response object for Retrieve commands
 */
interface RetrieveResponder<in T> : Responder<T, Multimap<ErrorTag, String>>