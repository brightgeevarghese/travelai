package com.example.travelai.feature.travelplan.uistate

data class TravelUiState (
    val prompt: String = "",
    val response: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)