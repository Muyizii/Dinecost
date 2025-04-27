package muyizii.s.dinecost.data.recordType

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface RecordTypeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(recordType: RecordType): Long

    suspend fun insertIfNotExists(recordType: RecordType): Boolean {
        return insert(recordType) != -1L
    }

    @Delete
    suspend fun delete(recordType: RecordType)

    @Update
    suspend fun update(recordType: RecordType)

    @Query("SELECT * FROM RecordType WHERE id = :id")
    suspend fun getRecordTypeById(id: Int): RecordType?

    @Query("SELECT * FROM RecordType ORDER BY id ASC")
    suspend fun getAllRecordType(): List<RecordType>
}