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

    companion object{
        const val PROMPT = "Analyze attached image. Return JSON ONLY.\n" +
                "Schema: { \"name\": str, \"calories\": int, \"type\": \"fruit\"|\"veg\"|\"dish\"|\"salad\"|\"error\", \"servingSize\": \"Qty (Weight)\", \"protein\": int, \"carbs\": int, \"fat\": int, \"fiber\": int, \"info\": [{ \"label\": \"str\", \"value\": \"str\" }] }\n" +
                "Rules:\n" +
                "1. If image is NOT food, set type=\"error\", name=\"Not Food\", numeric values=0, info=[].\n" +
                "2. If food, extract nutrition for full serving.\n" +
                "3. \"info\" includes micros (sodium, sugar, sat fat, etc).\n" +
                "4. \"value\" is ONLY Num+Unit (e.g. \"9 g\").\n" +
                "5. \"servingSize\" is concise."
    }

}