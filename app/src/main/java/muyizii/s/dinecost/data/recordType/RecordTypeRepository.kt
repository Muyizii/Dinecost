package muyizii.s.dinecost.data.recordType

interface RecordTypeRepository {
    suspend fun insert(recordType: RecordType): Long

    suspend fun insertIfNotExists(recordType: RecordType): Boolean

    suspend fun update(recordType: RecordType)

    suspend fun delete(recordType: RecordType)

    suspend fun getRecordTypeById(id: Int): RecordType?

    suspend fun getAllRecordType(): List<RecordType>
}