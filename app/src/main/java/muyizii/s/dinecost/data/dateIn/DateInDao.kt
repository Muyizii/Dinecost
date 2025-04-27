package muyizii.s.dinecost.data.dateIn

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface DateInDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(dateIn: DateIn)

    @Update
    suspend fun update(dateIn: DateIn)

    @Delete
    suspend fun delete(dateIn: DateIn)

    @Query("SELECT * FROM DateIn WHERE date = :date")
    suspend fun getDateInByDate(date: LocalDate): DateIn?

    @Query("SELECT * FROM DateIn LIMIT 1")
    suspend fun getFirstDateIn(): DateIn?

    @Query("SELECT * FROM DateIn LIMIT 1")
    fun getFirstDateInFlow(): Flow<DateIn?>

    @Query("SELECT * FROM DateIn")
    suspend fun getAllDateIn(): List<DateIn>
}