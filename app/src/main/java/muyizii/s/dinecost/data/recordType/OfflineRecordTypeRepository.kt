package muyizii.s.dinecost.data.recordType

class OfflineRecordTypeRepository(private val recordTypeDao: RecordTypeDao) : RecordTypeRepository {
    override suspend fun delete(recordType: RecordType) = recordTypeDao.delete(recordType)

    override suspend fun getRecordTypeById(id: Int): RecordType? = recordTypeDao.getRecordTypeById(id)

    override suspend fun insert(recordType: RecordType) = recordTypeDao.insert(recordType)

    override suspend fun insertIfNotExists(recordType: RecordType): Boolean = recordTypeDao.insertIfNotExists(recordType)

    override suspend fun update(recordType: RecordType) = recordTypeDao.update(recordType)

    override suspend fun getAllRecordType(): List<RecordType> = recordTypeDao.getAllRecordType()
}