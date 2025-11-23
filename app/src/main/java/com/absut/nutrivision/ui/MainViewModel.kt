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
                Log.e("MainViewModel", throwable.message ?: "Unknown error")
                _viewState.value = _viewState.value.copy(loading = false, error = throwable.message)
            }
        ) {
            if (image != null) {
                _viewState.value = _viewState.value.copy(
                    loading = true,
                    error = null
                )

                /////////////////
                delay(2000)
                val dummyResult = NutritionResult(
                    name = "Hawaiian Pizza",
                    calories = 520,
                    type = "Fast Food",
                    servingSize = "1 slice (150g)",
                    protein = 25,
                    carbs = 45,
                    fat = 22,
                    fiber = 5,
                    imagePath = _viewState.value.imagePath,
                    info = listOf(
                        InfoItem("Sodium", "1100mg"),
                        InfoItem("Total Sugars", "12g"),
                        InfoItem("Saturated Fat", "10g"),
                        InfoItem("Potassium", "300mg"),
                        InfoItem("Vitamin C", "15mg"),
                        InfoItem("Cholesterol", "60mg")
                    )
                )

                _viewState.value = _viewState.value.copy(
                    loading = false,
                    nutritionResult = dummyResult
                )
                /////////////////////

                /*val nutritionResultString = aiRepository.generateIngredients(image)
                val nutritionResult: NutritionResult = json.decodeFromString<NutritionResult>(nutritionResultString)

                _viewState.value = _viewState.value.copy(
                    loading = false,
                    nutritionResult = nutritionResult.copy(imagePath = _viewState.value.imagePath)
                )*/

                if (dummyResult.imagePath != null) {
                    saveNutritionRecord(dummyResult)
                } else {
                    Log.e("MainViewModel", "Image path is null, cannot save record.")
                }

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