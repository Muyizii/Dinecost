package muyizii.s.dinecost.data.detailRecord

import java.time.LocalDate

class OfflineDetailRecordRepository(private val detailRecordDao: DetailRecordDao): DetailRecordRepository {
    override suspend fun insert(detailRecord: DetailRecord) = detailRecordDao.insert(detailRecord)

    override suspend fun update(detailRecord: DetailRecord) = detailRecordDao.update(detailRecord)

    override suspend fun delete(detailRecord: DetailRecord) = detailRecordDao.delete(detailRecord)

    override suspend fun getDetailRecordById(id: Int): DetailRecord? = detailRecordDao.getDetailRecordById(id)

    override suspend fun getDetailRecordByDate(date: LocalDate): List<DetailRecord> = detailRecordDao.getDetailRecordByDate(date)

    override suspend fun getAllDetailRecord(): List<DetailRecord> = detailRecordDao.getAllDetailRecord()
}