package com.example.travelai.feature.travelplan.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelai.feature.travelplan.uistate.TravelUiState
import com.example.travelai.domain.repository.TravelPlanRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TravelViewModel(
    private val travelPlanRepository: TravelPlanRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(TravelUiState())
    val uiState: StateFlow<TravelUiState> = _uiState.asStateFlow()

    fun promptChanged(newPrompt: String) {
        _uiState.update {
            it.copy(prompt = newPrompt)
        }
    }

    fun generateTravelPan() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            runCatching {
                travelPlanRepository.generateTravelPan(
                    """
                    Prompt: Generate a short tour plan for the following request. The plan should have 2–5 bullets.
                Each bullet is one concise sentence. Output only the plan, no extra explanation.

                input: 2-day trip to Paris
                output:
                - Day 1: Visit the Eiffel Tower and enjoy a Seine River cruise.
                - Day 2: Explore the Louvre Museum and stroll through Montmartre.

                input: 3 days in Rome
                output:
                - Day 1: Explore the Colosseum and Roman Forum.
                - Day 2: Visit the Vatican Museums and St. Peter’s Basilica.
                - Day 3: Walk through the Pantheon and toss a coin at Trevi Fountain.

                input: ${uiState.value.prompt}
                """.trimIndent()
                )
            }.fold(
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(response = response ?: "", isLoading = false)
                    }
                },
                onFailure = {exception ->
                    _uiState.update {
                        it.copy(error = exception.message, isLoading = false)
                    }
                }
            )
        }
    }
}