package muyizii.s.dinecost.data.dateOut

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface DateOutDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(dateOut: DateOut)

    @Update
    suspend fun update(dateOut: DateOut)

    @Delete
    suspend fun delete(dateOut: DateOut)

    @Query("SELECT * FROM DateOut WHERE date = :date")
    suspend fun getDateOutByDate(date: LocalDate): DateOut?

    @Query("SELECT * FROM DateOut LIMIT 1")
    suspend fun getFirstDateOut(): DateOut?

    @Query("SELECT * FROM DateOut LIMIT 1")
    fun getFirstDateOutFlow(): Flow<DateOut?>

    @Query("SELECT * FROM DateOut")
    suspend fun getAllDateOut(): List<DateOut>
}