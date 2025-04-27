package muyizii.s.dinecost.data.detailRecord

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = "DetailRecord")
data class DetailRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: LocalDate,
    val time: LocalTime = LocalTime.now(),
    val amount: Double,
    val isIncome: Boolean,
    val isPatch: Boolean,
    val type: String,
    val subType: String,
    val detail: String
)