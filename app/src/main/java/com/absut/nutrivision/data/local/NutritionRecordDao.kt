package com.absut.nutrivision.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.absut.nutrivision.model.NutritionRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface NutritionRecordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: NutritionRecord)

    @Query("SELECT * FROM nutrition_records ORDER BY id DESC")
    fun getAllRecords(): Flow<List<NutritionRecord>>

    @Query("SELECT * FROM nutrition_records WHERE id = :id")
    fun getRecordById(id: Int): Flow<NutritionRecord>

    @Delete
    suspend fun deleteRecord(record: NutritionRecord)
}