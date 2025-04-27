package muyizii.s.dinecost.data.dateOut

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class OfflineDateOutRepository(private val dateOutDao: DateOutDao): DateOutRepository {
    override suspend fun insert(dateOut: DateOut) = dateOutDao.insert(dateOut)

    override suspend fun update(dateOut: DateOut) = dateOutDao.update(dateOut)

    override suspend fun delete(dateOut: DateOut) = dateOutDao.delete(dateOut)

    override suspend fun getDateOutByDate(date: LocalDate): DateOut? = dateOutDao.getDateOutByDate(date)

    override suspend fun getFirstDateOut(): DateOut? = dateOutDao.getFirstDateOut()

    override fun getFirstDateOutFlow(): Flow<DateOut?> = dateOutDao.getFirstDateOutFlow()

    override suspend fun getAllDateOut(): List<DateOut> = dateOutDao.getAllDateOut()
}