package com.absut.nutrivision.data.local

import androidx.room.TypeConverter
import com.absut.nutrivision.model.InfoItem
import kotlinx.serialization.json.Json

class InfoItemConverter {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromInfoItemList(info : List<InfoItem>) = json.encodeToString(info)

    @TypeConverter
    fun toInfoItemList(infoString : String) = json.decodeFromString<List<InfoItem>>(infoString)

}