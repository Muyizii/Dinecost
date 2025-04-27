package muyizii.s.dinecost.data.dateIn

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "DateIn")
data class DateIn(
    @PrimaryKey
    val date: LocalDate,
    val amount: Double,
)