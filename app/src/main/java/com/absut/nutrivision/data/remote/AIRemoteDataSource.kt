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
            text(PROMPT)
        }

        val response = generativeModel.generateContent(prompt)
        return response.text.orEmpty()
    }

    companion object {

        val PROMPT = """
    Analyze attached image. Return raw JSON ONLY. No markdown or backticks.
    Schema: { "name": str, "calories": int, "type": "fruit"|"veg"|"dish"|"salad"|"error", "servingSize": "Qty (Weight)", "protein": int, "carbs": int, "fat": int, "fiber": int, "info": [{ "label": "str", "value": "str" }] }
    Rules:
    1. If image is NOT food: set type="error", name="Not Food", all numbers=0, info=[].
    2. If food: extract nutrition for full serving shown.
    3. "info" array must include: Sodium, Sugar, Saturated Fat, Potassium, Cholesterol, Vitamins.
    4. "value" format is STRICTLY Number+Unit (e.g. "9 g", "250 mg"). No extra text.
    5. "servingSize" is concise (e.g. "1 Plate (740g)").
""".trimIndent()

    }

}