package com.absut.nutrivision.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.absut.nutrivision.data.local.InfoItemConverter
import kotlinx.serialization.Serializable

@Entity(tableName = "nutrition_records")
@TypeConverters(InfoItemConverter::class)
@Serializable
data class NutritionRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val imagePath: String, // Path to the image in temp storage
    val name: String,
    val calories: Int,
    val type: String,
    val servingSize: String,
    val protein: Int,
    val carbs: Int,
    val fat: Int,
    val fiber: Int,
    val info: List<InfoItem>
)