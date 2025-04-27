package muyizii.s.dinecost.data.dateOut

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "DateOut")
data class DateOut (
    @PrimaryKey
    val date: LocalDate,
    val amount: Double
)