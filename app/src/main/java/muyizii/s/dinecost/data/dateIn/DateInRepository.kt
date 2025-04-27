package muyizii.s.dinecost.data.dateIn

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface DateInRepository {
    suspend fun insert(dateIn: DateIn)

    suspend fun update(dateIn: DateIn)

    suspend fun delete(dateIn: DateIn)

    suspend fun getDateInByDate(date: LocalDate): DateIn?

    suspend fun getFirstDateIn(): DateIn?

    fun getFirstDateInFlow(): Flow<DateIn?>

    suspend fun getAllDateIn(): List<DateIn>
}