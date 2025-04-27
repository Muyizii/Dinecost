package muyizii.s.dinecost.data.recordType

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "RecordType",
    indices = [Index(value = ["name", "isIncome"], unique = true)]
)
data class RecordType (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val isIncome: Boolean = false,
)