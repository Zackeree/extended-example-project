package com.example.project.contract.responder

import com.google.common.collect.Multimap

/**
 * Abstract response object for Create commands
 */
interface CreateResponder<T>: Responder<Long, Multimap<T, String>>