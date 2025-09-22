package com.example.travelai

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travelai.data.ai.GenerativeModelService
import com.example.travelai.data.repository.OnDeviceTravelPlanRepository
import com.example.travelai.feature.travelplan.viewmodel.TravelViewModel

@Composable
fun TravelPlanScreen(modifier: Modifier = Modifier) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
        ) {
            val context = LocalContext.current
            val viewModel: TravelViewModel = viewModel {
                TravelViewModel(
                    OnDeviceTravelPlanRepository(
                        GenerativeModelService.getModel(context),
                        context = context
                    )
                )
            }
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            OutlinedTextField(
                value = uiState.prompt,
                onValueChange = { viewModel.promptChanged(it) },
                label = { Text("Prompt") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = { viewModel.generateTravelPan() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Generate Travel Plan")
            }
            when {
                uiState.isLoading -> {
                    Text("Loading...")
                }
                uiState.response != null -> {
                    Text(uiState.response)
                }
                uiState.error != null -> {
                    Text("Error: ${uiState.error}")
                }
            }
        }
    }
}