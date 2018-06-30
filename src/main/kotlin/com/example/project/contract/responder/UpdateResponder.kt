package com.example.project.contract.responder

import com.google.common.collect.Multimap

/**
 * Abstract update response object for Update commands
 */
interface UpdateResponder<T> : Responder<Long, Multimap<T, String>>