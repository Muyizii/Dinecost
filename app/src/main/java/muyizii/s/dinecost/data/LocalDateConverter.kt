package muyizii.s.dinecost.data

import androidx.room.TypeConverter
import java.time.LocalDate

class LocalDateConverter {
    @TypeConverter
    fun fromLocalDate(date: LocalDate) = date.toString()

    @TypeConverter
    fun toLocalDate(dateString: String) = LocalDate.parse(dateString)
}