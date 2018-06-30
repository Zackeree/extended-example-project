package com.example.project.contract.responder

import com.google.common.collect.Multimap
import org.springframework.data.domain.Page

interface PageResponder<T, E>: Responder<Page<T>, Multimap<E, String>>