package com.example.aulaplus.api

import com.example.aulaplus.api.dto.CursoDto
import com.example.aulaplus.api.dto.EvaluacionDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("courses")
    suspend fun getCourses(@Query("email") email: String): List<CursoDto>

    @GET("evaluations")
    suspend fun getEvaluations(@Query("email") email: String): List<EvaluacionDto>
}
