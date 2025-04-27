package muyizii.s.dinecost.data.detailRecord

import java.time.LocalDate

interface DetailRecordRepository {
    suspend fun insert(detailRecord: DetailRecord)

    suspend fun update(detailRecord: DetailRecord)

    suspend fun delete(detailRecord: DetailRecord)

    suspend fun getDetailRecordById(id: Int): DetailRecord?

    suspend fun getDetailRecordByDate(date: LocalDate): List<DetailRecord>

    suspend fun getAllDetailRecord(): List<DetailRecord>
}