package com.example.project.contract.responder

import com.google.common.collect.Multimap

/**
 * Abstract retrieve response object for Retrieve commands
 */
interface RetrieveResponder<in T, ERROR_TAG> : Responder<T, Multimap<ERROR_TAG, String>>