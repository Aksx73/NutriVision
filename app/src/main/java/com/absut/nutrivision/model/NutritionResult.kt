package com.absut.nutrivision.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class InfoItem(
    val label: String,
    val value: String
) : Parcelable

@Serializable
@Parcelize
data class NutritionResult(
    val imagePath : String?,
    val name: String,
    val calories: Int,
    val type: String,
    val servingSize: String,
    val protein: Int,
    val carbs: Int,
    val fat: Int,
    val fiber: Int,
    val info: List<InfoItem>
) : Parcelable
