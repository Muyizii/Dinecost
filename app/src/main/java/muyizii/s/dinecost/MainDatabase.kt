package muyizii.s.dinecost

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import muyizii.s.dinecost.data.LocalDateConverter
import muyizii.s.dinecost.data.LocalTimeConverter
import muyizii.s.dinecost.data.dateIn.DateIn
import muyizii.s.dinecost.data.dateIn.DateInDao
import muyizii.s.dinecost.data.dateOut.DateOut
import muyizii.s.dinecost.data.dateOut.DateOutDao
import muyizii.s.dinecost.data.detailRecord.DetailRecord
import muyizii.s.dinecost.data.detailRecord.DetailRecordDao
import muyizii.s.dinecost.data.recordType.RecordType
import muyizii.s.dinecost.data.recordType.RecordTypeDao

@Database(
    entities = [DateIn::class, DateOut::class, DetailRecord::class, RecordType::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(LocalDateConverter::class, LocalTimeConverter::class)
abstract class MainDatabase : RoomDatabase() {

    abstract fun dateInDao(): DateInDao
    abstract fun dateOutDao(): DateOutDao
    abstract fun detailRecordDao(): DetailRecordDao
    abstract fun recordTypeDao(): RecordTypeDao

    companion object {
        @Volatile
        private var Instance: MainDatabase? = null

        fun getDatabase(context: Context): MainDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, MainDatabase::class.java, "mainDatabase")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}