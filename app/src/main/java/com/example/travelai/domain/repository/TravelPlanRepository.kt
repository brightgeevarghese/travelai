package com.example.travelai.domain.repository

interface TravelPlanRepository {
    suspend fun generateTravelPan(prompt: String): String?
}