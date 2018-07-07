package com.example.project.contract.responder

import com.google.common.collect.HashMultimap

/**
 * Abstract response object for Delete commands
 */
interface DeleteResponder<T> : Responder<Long, HashMultimap<T, String>>