package muyizii.s.dinecost

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import muyizii.s.dinecost.data.dateIn.DateInRepository
import muyizii.s.dinecost.data.dateIn.OfflineDateInRepository
import muyizii.s.dinecost.data.dateOut.DateOutRepository
import muyizii.s.dinecost.data.dateOut.OfflineDateOutRepository
import muyizii.s.dinecost.data.detailRecord.DetailRecordRepository
import muyizii.s.dinecost.data.detailRecord.OfflineDetailRecordRepository
import muyizii.s.dinecost.data.recordType.OfflineRecordTypeRepository
import muyizii.s.dinecost.data.recordType.RecordTypeRepository
import muyizii.s.dinecost.helpers.DatabaseHelper
import muyizii.s.dinecost.helpers.NotificationHelper
import muyizii.s.dinecost.helpers.PermissionHelper
import muyizii.s.dinecost.helpers.SettingHelper
import muyizii.s.dinecost.sms.SmsRepository
import muyizii.s.dinecost.sms.SmsRepositoryImpl

interface MainAppContainer {
    val dateInRepository: DateInRepository
    val dateOutRepository: DateOutRepository
    val detailRecordRepository: DetailRecordRepository
    val recordTypeRepository: RecordTypeRepository
    val settingDataStore: DataStore<Preferences>
    val databaseHelper: DatabaseHelper
    val settingHelper: SettingHelper
    val notificationHelper: NotificationHelper
    val permissionHelper: PermissionHelper
    val smsRepository: SmsRepository
}

class MainAppDataContainer(private val context: Context) : MainAppContainer {
    override val dateInRepository: DateInRepository by lazy {
        OfflineDateInRepository(MainDatabase.getDatabase(context).dateInDao())
    }
    override val dateOutRepository: DateOutRepository by lazy {
        OfflineDateOutRepository(MainDatabase.getDatabase(context).dateOutDao())
    }
    override val detailRecordRepository: DetailRecordRepository by lazy {
        OfflineDetailRecordRepository(MainDatabase.getDatabase(context).detailRecordDao())
    }
    override val recordTypeRepository: RecordTypeRepository by lazy {
        OfflineRecordTypeRepository(MainDatabase.getDatabase(context).recordTypeDao())
    }
    override val settingDataStore: DataStore<Preferences> by lazy {
        PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile("main_setting") }
        )
    }

    override val databaseHelper: DatabaseHelper = DatabaseHelper(
        dateInRepository = dateInRepository,
        dateOutRepository = dateOutRepository,
        detailRecordRepository = detailRecordRepository,
        recordTypeRepository = recordTypeRepository
    )
    override val settingHelper: SettingHelper = SettingHelper(
        settingDataStore = settingDataStore
    )
    override val notificationHelper: NotificationHelper = NotificationHelper(
        context = context
    )
    override val permissionHelper: PermissionHelper = PermissionHelper(
        context = context
    )
    override val smsRepository: SmsRepository by lazy {
        SmsRepositoryImpl(
            context = context,
            databaseHelper = databaseHelper,
            notificationHelper = notificationHelper
        )
    }
}