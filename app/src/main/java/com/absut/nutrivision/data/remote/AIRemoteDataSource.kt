package com.absut.nutrivision.data.remote

import android.graphics.Bitmap
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.type.PublicPreviewAPI
import com.google.firebase.ai.type.content
import javax.inject.Inject

@OptIn(PublicPreviewAPI::class)
class AIRemoteDataSource @Inject constructor(
    private val generativeModel: GenerativeModel,
) {
    suspend fun generateNutrition(image: Bitmap): String {
        val prompt = content {
            image(image)
            text("Please analyze this image and list all visible food ingredients. " +
                    "Format the response as a comma-separated list of ingredients. " +
                    "Be specific with measurements where possible, " +
                    "but focus on identifying the ingredients accurately.")
        }

        val response = generativeModel.generateContent(prompt)
        return response.text.orEmpty()
    }

}