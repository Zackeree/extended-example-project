package com.example.project.contract.responder

import com.google.common.collect.Multimap

interface ListResponder<T, E>: Responder<List<T>, Multimap<E, String>>