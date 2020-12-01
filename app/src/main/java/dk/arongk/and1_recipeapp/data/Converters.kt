package dk.arongk.and1_recipeapp.data

import androidx.room.TypeConverter
import java.util.*

class Converters {
    @TypeConverter
    fun uuidToString(value: String?): UUID? {
        return value?.let { UUID.fromString(it) }
    }

    @TypeConverter
    fun stringToUUID(uuid: UUID?): String? {
        return uuid.toString()
    }
}