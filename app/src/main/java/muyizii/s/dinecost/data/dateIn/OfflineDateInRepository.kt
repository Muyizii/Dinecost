package muyizii.s.dinecost.data.dateIn

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class OfflineDateInRepository(private val dateInDao: DateInDao) : DateInRepository {
    override suspend fun insert(dateIn: DateIn) = dateInDao.insert(dateIn)

    override suspend fun update(dateIn: DateIn) = dateInDao.update(dateIn)

    override suspend fun delete(dateIn: DateIn) = dateInDao.delete(dateIn)

    override suspend fun getDateInByDate(date: LocalDate): DateIn? = dateInDao.getDateInByDate(date)

    override suspend fun getFirstDateIn(): DateIn? = dateInDao.getFirstDateIn()

    override fun getFirstDateInFlow(): Flow<DateIn?> = dateInDao.getFirstDateInFlow()

    override suspend fun getAllDateIn(): List<DateIn> = dateInDao.getAllDateIn()
}