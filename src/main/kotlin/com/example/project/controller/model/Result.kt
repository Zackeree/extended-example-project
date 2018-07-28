package com.example.project.controller.model

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializable
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.jsontype.TypeSerializer
import org.springframework.http.HttpStatus

/**
 * Class used to send information back to the client
 * @property data any onSuccess data
 * @property errors a map of the errors
 */
class Result(
        var data: Any?=null,
        var errors: Map<String, Collection<String>>?=null,
        var status: HttpStatus? = null
) : JsonSerializable {
    /**
     * Adds logic to our constructor. If status is null, set it
     * to OK if errors is null or BAD_REQUEST if errors is not null
     */
    init {
        if (status == null) status = if (errors == null) HttpStatus.OK
        else HttpStatus.BAD_REQUEST
    }

    /**
     * Allows us to customize how we serialize our object when we respond to
     * the client with it
     */
    override fun serialize(gen: JsonGenerator?, serializers: SerializerProvider?) {
        gen?.writeStartObject();

        gen?.writeStringField("statusCode", status?.value().toString())
        gen?.writeStringField("statusReason", status?.reasonPhrase)

        if (data != null) {
            gen?.writeObjectField("data", data)
        }

        if (errors != null && errors!!.isNotEmpty()) {
            gen?.writeObjectField("errors", errors)
        }

        gen?.writeEndObject()
    }

    /**
     * Calls our other serialize method above
     */
    override fun serializeWithType(gen: JsonGenerator?, serializers: SerializerProvider?, typeSer: TypeSerializer?) {
        serialize(gen, serializers)
    }
}