package muyizii.s.dinecost.data.dateOut

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface DateOutRepository {
    suspend fun insert(dateOut: DateOut)

    suspend fun update(dateOut: DateOut)

    suspend fun delete(dateOut: DateOut)

    suspend fun getDateOutByDate(date: LocalDate): DateOut?

    suspend fun getFirstDateOut(): DateOut?

    fun getFirstDateOutFlow(): Flow<DateOut?>

    suspend fun getAllDateOut(): List<DateOut>
}