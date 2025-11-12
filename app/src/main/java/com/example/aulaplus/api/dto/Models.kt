package com.example.aulaplus.api.dto

data class CursoDto(
    val id: String,
    val nombre: String,
    val docente: String,
    val estado: String
)

data class EvaluacionDto(
    val id: String,
    val evaluacion: String,
    val curso: String,
    val estado: String // o nota
)
