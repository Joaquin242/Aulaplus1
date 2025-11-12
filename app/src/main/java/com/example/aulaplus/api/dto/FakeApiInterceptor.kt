package com.example.aulaplus.api

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class FakeApiInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val path = chain.request().url.encodedPath
        val media = "application/json".toMediaType()

        val body = when {
            path.endsWith("/courses") -> """
                [
                  {"id":"c1","nombre":"FullStack II","docente":"Prof. R. Arce","estado":"Cursando"},
                  {"id":"c2","nombre":"Desarrollo Apps","docente":"Prof. J. Thompson","estado":"Cursando"},
                  {"id":"c3","nombre":"Gestión Ágil","docente":"Prof. C. Pérez","estado":"Aprobado"}
                ]
            """.trimIndent()

            path.endsWith("/evaluations") -> """
                [
                  {"id":"e1","evaluacion":"Examen Final","curso":"FullStack II","estado":"Programado 10/12"},
                  {"id":"e2","evaluacion":"Prueba Parcial","curso":"Desarrollo Apps","estado":"Pendiente"},
                  {"id":"e3","evaluacion":"Proyecto","curso":"Gestión Ágil","estado":"Nota: 6.3"}
                ]
            """.trimIndent()

            else -> "[]"
        }

        return Response.Builder()
            .code(200)
            .message("OK")
            .request(chain.request())
            .protocol(Protocol.HTTP_1_1)
            .body(body.toResponseBody(media))
            .build()
    }
}
