package com.absut.nutrivision.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.absut.nutrivision.model.NutritionRecord

@Database(entities = [NutritionRecord::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun nutritionRecordDao(): NutritionRecordDao
}