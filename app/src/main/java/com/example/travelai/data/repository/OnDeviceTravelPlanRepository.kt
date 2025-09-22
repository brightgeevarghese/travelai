package com.example.travelai.data.repository

import android.content.Context
import com.example.travelai.domain.repository.TravelPlanRepository
import com.google.ai.edge.aicore.GenerativeModel

class OnDeviceTravelPlanRepository(
    private val generativeModel: GenerativeModel,
    private val context: Context
): TravelPlanRepository {
    override suspend fun generateTravelPan(prompt: String): String? {
        //generateContent() is a suspend function
        return generativeModel.generateContent(prompt).text
    }
}