package com.example.aulaplus.repository

import com.example.aulaplus.api.ApiClient
import com.example.aulaplus.api.dto.EvaluacionDto

class EvaluationsRepository {
    suspend fun fetch(email: String): List<EvaluacionDto> =
        ApiClient.api.getEvaluations(email)
}
