package com.absut.nutrivision.data.repository

import android.graphics.Bitmap
import com.absut.nutrivision.data.local.NutritionRecordDao
import com.absut.nutrivision.data.remote.AIRemoteDataSource
import com.absut.nutrivision.model.NutritionRecord
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AIRepository @Inject constructor(
    private val aiRemoteDataSource: AIRemoteDataSource,
    private val nutritionRecordDao: NutritionRecordDao
) {
    suspend fun generateIngredients(image: Bitmap): String {
        return aiRemoteDataSource.generateNutrition(image)
    }

    suspend fun saveNutritionRecord(record: NutritionRecord) {
        nutritionRecordDao.insertRecord(record)
    }

    fun getAllRecords(): Flow<List<NutritionRecord>> {
        return nutritionRecordDao.getAllRecords()
    }

    fun getRecordById(id: Int): Flow<NutritionRecord> {
        return nutritionRecordDao.getRecordById(id)
    }

    suspend fun deleteNutritionRecord(record: NutritionRecord) {
        nutritionRecordDao.deleteRecord(record)
    }
}