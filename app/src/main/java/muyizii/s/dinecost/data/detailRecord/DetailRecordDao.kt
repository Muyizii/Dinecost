package muyizii.s.dinecost.data.detailRecord

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import java.time.LocalDate

@Dao
interface DetailRecordDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(detailRecord: DetailRecord)

    @Delete
    suspend fun delete(detailRecord: DetailRecord)

    @Update
    suspend fun update(detailRecord: DetailRecord)

    @Query("SELECT * FROM DetailRecord WHERE id = :id")
    suspend fun getDetailRecordById(id: Int): DetailRecord?

    @Query("SELECT * FROM DetailRecord WHERE date = :date")
    suspend fun getDetailRecordByDate(date: LocalDate): List<DetailRecord>

    @Query("SELECT * FROM DetailRecord")
    suspend fun getAllDetailRecord(): List<DetailRecord>
}