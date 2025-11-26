package com.absut.nutrivision.ui

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.absut.nutrivision.data.repository.AIRepository
import com.absut.nutrivision.model.InfoItem
import com.absut.nutrivision.model.NutritionRecord
import com.absut.nutrivision.model.NutritionResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

private val json = Json { ignoreUnknownKeys = true }

@HiltViewModel
class MainViewModel @Inject constructor(
    private val aiRepository: AIRepository
) : ViewModel() {

    private val _viewState = MutableStateFlow(AppViewState())
    val viewState: StateFlow<AppViewState>
        get() = _viewState.asStateFlow()

    val savedRecords = aiRepository.getAllRecords()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun onImageCaptured(bitmap: Bitmap?, imagePath: String? = null) {
        _viewState.value = _viewState.value.copy(bitmap = bitmap, imagePath = imagePath)
    }

    fun generateNutrition(image: Bitmap?) {
        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                Log.e("MainViewModel", "Error: ${throwable.message}", throwable)
                _viewState.value = _viewState.value.copy(
                    loading = false,
                    error = throwable.message ?: "Unknown error occurred"
                )
            }
        ) {
            if (image == null) {
                _viewState.value = _viewState.value.copy(error = "No image to analyze")
                return@launch
            }

            _viewState.value = _viewState.value.copy(loading = true, error = null)

            try {
                delay(2000)
                //val nutritionResultString = aiRepository.generateIngredients(image)
                val nutritionResultString = "{\n" +
                        "  \"name\": \"Greek Salad with Feta\",\n" +
                        "  \"calories\": 320,\n" +
                        "  \"type\": \"salad\",\n" +
                        "  \"servingSize\": \"1 Bowl (350g)\",\n" +
                        "  \"protein\": 8,\n" +
                        "  \"carbs\": 12,\n" +
                        "  \"fat\": 26,\n" +
                        "  \"fiber\": 4,\n" +
                        "  \"info\": [\n" +
                        "    {\n" +
                        "      \"label\": \"Sodium\",\n" +
                        "      \"value\": \"680 mg\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"label\": \"Sugars\",\n" +
                        "      \"value\": \"7 g\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"label\": \"Saturated Fat\",\n" +
                        "      \"value\": \"6 g\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"label\": \"Calcium\",\n" +
                        "      \"value\": \"18% DV\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"label\": \"Vitamin A\",\n" +
                        "      \"value\": \"25% DV\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"label\": \"Vitamin C\",\n" +
                        "      \"value\": \"30% DV\"\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}"

                // Parse JSON with error handling
                val nutritionResult: NutritionResult = try {
                    json.decodeFromString<NutritionResult>(nutritionResultString)
                } catch (e: Exception) {
                    throw Exception("Failed to parse nutrition data: ${e.message}")
                }

                if (nutritionResult.type == "error") {
                    throw Exception("This image does not look like food.")
                }

                // Validate image path
                if (_viewState.value.imagePath == null) {
                    throw Exception("Image path is missing")
                }

                val resultWithPath = nutritionResult.copy(imagePath = _viewState.value.imagePath)

                _viewState.value = _viewState.value.copy(
                    loading = false,
                    nutritionResult = resultWithPath
                )

                //saveNutritionRecord(resultWithPath)

            } catch (e: Exception) {
                Log.e("MainViewModel", "Failed to generate nutrition", e)
                _viewState.value = _viewState.value.copy(
                    loading = false,
                    error = when {
                        e.message?.contains("Unable to resolve host") == true -> "No internet connection"
                        e.message?.contains("timeout") == true -> "Request timed out"
                        e.message?.contains("parse") == true -> e.message
                        e.message?.contains("food") == true -> e.message
                        else -> "Failed to analyze image: ${e.message}"
                    }
                )
            }
        }
    }

    fun saveNutritionRecord(result: NutritionResult) {
        viewModelScope.launch {
            val record = NutritionRecord(
                imagePath = result.imagePath!!,
                name = result.name,
                calories = result.calories,
                type = result.type,
                servingSize = result.servingSize,
                protein = result.protein,
                carbs = result.carbs,
                fat = result.fat,
                fiber = result.fiber,
                info = result.info
            )
            aiRepository.saveNutritionRecord(record)
        }
    }

    fun deleteRecord(record: NutritionRecord) {
        viewModelScope.launch {
            aiRepository.deleteNutritionRecord(record)
            if (selectedRecord?.id == record.id) {
                setSelectedRecord(null)
            }
        }
    }

    fun clearError() {
        _viewState.value = _viewState.value.copy(error = null)
    }

    /*fun getRecordById(id: Int): Flow<NutritionRecord> {
        return aiRepository.getRecordById(id)
    }*/

    fun clearNutritionResult() {
        _viewState.value = _viewState.value.copy(
            nutritionResult = null,
            bitmap = null,
            imagePath = null,
            error = null
        )
    }

    private var selectedRecord : NutritionRecord? = null
    fun setSelectedRecord(record: NutritionRecord?) {
        selectedRecord = record
    }
    fun getSelectedRecord() : NutritionRecord? = selectedRecord

}

data class AppViewState(
    val bitmap: Bitmap? = null,
    val imagePath: String? = null,
    val nutritionResult: NutritionResult? = null,
    val loading: Boolean = false,
    val error: String? = null
)