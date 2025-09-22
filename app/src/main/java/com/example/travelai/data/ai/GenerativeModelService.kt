package com.example.travelai.data.ai

import android.content.Context
import com.google.ai.edge.aicore.DownloadConfig
import com.google.ai.edge.aicore.GenerationConfig
import com.google.ai.edge.aicore.GenerativeModel
import com.google.ai.edge.aicore.generationConfig
import kotlin.concurrent.Volatile

object GenerativeModelService {
    @Volatile
    private var model: GenerativeModel? = null

    fun getModel(context: Context): GenerativeModel {
        val generationConfig: GenerationConfig = generationConfig {
            this.context = context.applicationContext
            temperature = 0.2f
            topK = 16
            maxOutputTokens = 128
        }
        model?.let {
            return it
        }
        val downloadConfig: DownloadConfig = DownloadConfig()
        return model?: synchronized(this) {
            val generativeModel = GenerativeModel(generationConfig, downloadConfig)
            model = generativeModel
            generativeModel
        }
    }
}