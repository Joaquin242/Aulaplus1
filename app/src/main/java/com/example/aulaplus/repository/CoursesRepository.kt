package com.example.aulaplus.repository

import com.example.aulaplus.api.ApiClient
import com.example.aulaplus.api.dto.CursoDto

class CoursesRepository {
    suspend fun fetch(email: String): List<CursoDto> =
        ApiClient.api.getCourses(email)
}
