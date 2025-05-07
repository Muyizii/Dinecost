package muyizii.s.dinecost.helpers

import android.util.Log
import kotlinx.coroutines.flow.Flow
import muyizii.s.dinecost.data.dateIn.DateIn
import muyizii.s.dinecost.data.dateIn.DateInRepository
import muyizii.s.dinecost.data.dateOut.DateOut
import muyizii.s.dinecost.data.dateOut.DateOutRepository
import muyizii.s.dinecost.data.detailRecord.DetailRecord
import muyizii.s.dinecost.data.detailRecord.DetailRecordRepository
import muyizii.s.dinecost.data.recordType.RecordType
import muyizii.s.dinecost.data.recordType.RecordTypeRepository
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import kotlin.collections.iterator

class DatabaseHelper(
    private val dateInRepository: DateInRepository,
    private val dateOutRepository: DateOutRepository,
    private val detailRecordRepository: DetailRecordRepository,
    private val recordTypeRepository: RecordTypeRepository,
) {
    private fun Double.roundToDecimal(decimals: Int): Double {
        return BigDecimal(this).setScale(decimals, RoundingMode.HALF_UP).toDouble()
    }

    suspend fun getAllRecordType(): List<RecordType> {
        return recordTypeRepository.getAllRecordType()
        Log.d("DatabaseHelper", "读取了RecordType表的所有数据")
    }

    suspend fun addRecordType(recordType: RecordType): Boolean {
        return recordTypeRepository.insertIfNotExists(recordType)
        Log.d("DatabaseHelper", "向RecordType表中添加了新数据")
    }

    suspend fun deleteRecordType(recordType: RecordType) {
        recordTypeRepository.delete(recordType)
        Log.d("DatabaseHelper", "向RecordType表中删除了数据")
    }


    suspend fun getDateOutByDate(date: LocalDate): DateOut? {
        return dateOutRepository.getDateOutByDate(date)
        Log.d("DatabaseHelper", "尝试读取了DateOut表中日期为" + date + "的数据")
    }

    suspend fun getDateInByDate(date: LocalDate): DateIn? {
        return dateInRepository.getDateInByDate(date)
        Log.d("DatabaseHelper", "尝试读取了DateIn表中日期为" + date + "的数据")
    }

    private suspend fun updateDateOutByDateAdd(date: LocalDate, amount: Double) {
        updateFirstDateOut(isAdd = true, amount = amount)

        val oldDateOut = dateOutRepository.getDateOutByDate(date)
        if (oldDateOut == null) {
            dateOutRepository.insert(
                DateOut(
                    date = date,
                    amount = amount.roundToDecimal(2)
                )
            )
            Log.d("DatabaseHelper", "向DateOut表中添加了数据")
        } else {
            dateOutRepository.update(
                DateOut(
                    date = date,
                    amount = (oldDateOut.amount + amount).roundToDecimal(2)
                )
            )
            Log.d("DatabaseHelper", "向DateOut表中修改了数据：增加")
        }
    }

    private suspend fun updateDateOutByDateMinus(date: LocalDate, amount: Double) {
        updateFirstDateOut(isAdd = false, amount = amount)

        val oldDateOut = dateOutRepository.getDateOutByDate(date)
        if (oldDateOut == null) {
            Log.e("DatabaseHelper", "调用了因为删除详细记录导致的日支出减少，但并未读取到该日支出")
        } else {
            dateOutRepository.update(
                DateOut(
                    date = date,
                    amount = (oldDateOut.amount - amount).roundToDecimal(2)
                )
            )
            Log.d("DatabaseHelper", "向DateOut表中修改了数据：减少")
        }
    }

    private suspend fun updateDateInByDateAdd(date: LocalDate, amount: Double) {
        updateFirstDateIn(isAdd = true, amount = amount)

        val oldDateIn = dateInRepository.getDateInByDate(date)
        if (oldDateIn == null) {
            dateInRepository.insert(
                DateIn(
                    date = date,
                    amount = amount.roundToDecimal(2)
                )
            )
            Log.d("DatabaseHelper", "向DateIn表中添加了数据")
        } else {
            dateInRepository.update(
                DateIn(
                    date = date,
                    amount = (oldDateIn.amount + amount).roundToDecimal(2)
                )
            )
            Log.d("DatabaseHelper", "向DateIn表中修改了数据：增加")
        }
    }

    private suspend fun updateDateInByDateMinus(date: LocalDate, amount: Double) {
        updateFirstDateIn(isAdd = false, amount = amount)

        val oldDateIn = dateInRepository.getDateInByDate(date)
        if (oldDateIn == null) {
            Log.d("DatabaseHelper", "调用了因为删除详细记录导致的日收入减少，但并未读取到该日收入")
        } else {
            dateInRepository.update(
                DateIn(
                    date = date,
                    amount = (oldDateIn.amount - amount).roundToDecimal(2)
                )
            )
            Log.d("DatabaseHelper", "向DateIn表中修改了数据：减少")
        }
    }

    suspend fun getFirstDateOut(): DateOut? {
        return dateOutRepository.getFirstDateOut()
        Log.d("DatabaseHelper", "尝试读取DateOut表第一个数据")
    }

    suspend fun getFirstDateIn(): DateIn? {
        return dateInRepository.getFirstDateIn()
        Log.d("DatabaseHelper", "尝试读取DateIn表第一个数据")
    }

    fun getFirstDateInFlow(): Flow<DateIn?> {
        Log.d("DatabaseHelper", "尝试获取DateIn流")
        return dateInRepository.getFirstDateInFlow()
    }

    fun getFirstDateOutFlow(): Flow<DateOut?> {
        Log.d("DatabaseHelper", "尝试获取DateOut流")
        return dateOutRepository.getFirstDateOutFlow()
    }

    private suspend fun updateFirstDateOut(isAdd: Boolean, amount: Double) {
        val firstDateOut = getFirstDateOut()
        dateOutRepository.update(
            DateOut(
                date = firstDateOut!!.date,
                amount = (if (isAdd) firstDateOut.amount + amount else firstDateOut.amount - amount).roundToDecimal(2)
            )
        )
        Log.d("DatabaseHelper", "修改了总的支出数据" + if (isAdd) "增加$amount" else "减少$amount")
    }

    private suspend fun updateFirstDateIn(isAdd: Boolean, amount: Double) {
        val firstDateIn = getFirstDateIn()
        dateInRepository.update(
            DateIn(
                date = firstDateIn!!.date,
                amount = (if (isAdd) firstDateIn.amount + amount else firstDateIn.amount - amount).roundToDecimal(2)
            )
        )
        Log.d("DatabaseHelper", "修改了总的收入数据")
    }

    suspend fun initFirstDateOut(date: LocalDate) {
        dateOutRepository.insert(
            DateOut(
                date = date,
                amount = 0.0
            )
        )
        Log.d("DatabaseHelper", "添加了第一条DateOut数据")
    }

    suspend fun initFirstDateIn(date: LocalDate) {
        dateInRepository.insert(
            DateIn(
                date = date,
                amount = 0.0
            )
        )
        Log.d("DatabaseHelper", "添加了第一条DateIn数据")
    }

    suspend fun reFlushDatabaseByDetailRecord(): Boolean {
        val recordList: List<DetailRecord> = detailRecordRepository.getAllDetailRecord()
        var dateInMap = mutableMapOf<LocalDate, Double>().withDefault { 0.0 }
        var dateOutMap = mutableMapOf<LocalDate, Double>().withDefault { 0.0 }
        var totalIn: Double = 0.0
        var totalOut: Double = 0.0

        for (i in recordList) {
            if (i.isIncome) {
                totalIn += i.amount
                dateInMap[i.date] = dateInMap.getValue(i.date) + i.amount
            } else {
                totalOut += i.amount
                dateOutMap[i.date] = dateOutMap.getValue(i.date) + i.amount
            }
        }

        for (i in dateInMap) {
            dateInRepository.update(DateIn(date = i.key, amount = i.value))
        }
        for (i in dateOutMap) {
            dateOutRepository.update(DateOut(date = i.key, amount = i.value))
        }
        dateInRepository.update(
            DateIn(
                date = dateInRepository.getFirstDateIn()!!.date,
                amount = totalIn
            )
        )
        dateOutRepository.update(
            DateOut(
                date = dateOutRepository.getFirstDateOut()!!.date,
                amount = totalOut
            )
        )
        Log.d("DatabaseHelper", "更新完毕")
        return false
    }


    suspend fun getDetailRecordByDate(date: LocalDate): List<DetailRecord> {
        return detailRecordRepository.getDetailRecordByDate(date)
        Log.d("DatabaseHelper", "读取了DetailRecord表中所有日期为" + date + "的数据")
    }

    suspend fun checkHasPatchByDate(date: LocalDate): Boolean {
        val detailRecordDate = detailRecordRepository.getDetailRecordByDate(date)
        for (i in detailRecordDate) {
            if (i.isPatch) {
                Log.d("DatabaseHelper", "判断日期" + date + "存在自动记账未分类的数据")
                return true
            }
        }
        Log.d("DatabaseHelper", "判断日期" + date + "不存在自动记账未分类的数据")
        return false
    }

    suspend fun getAllDetailRecord(): List<DetailRecord>{
        return detailRecordRepository.getAllDetailRecord()
    }

    suspend fun getDetailRecordById(id: Int): DetailRecord? {
        return detailRecordRepository.getDetailRecordById(id)
    }

    suspend fun addDetailRecord(detailRecord: DetailRecord) {
        if (detailRecord.isIncome) {
            updateDateInByDateAdd(detailRecord.date, detailRecord.amount)
        } else {
            updateDateOutByDateAdd(detailRecord.date, detailRecord.amount)
        }
        detailRecordRepository.insert(detailRecord)
        Log.d("DatabaseHelper", "向DetailRecord表中添加了新数据")
    }

    suspend fun deleteDetailRecord(detailRecord: DetailRecord) {
        if (detailRecord.isIncome) {
            updateDateInByDateMinus(detailRecord.date, detailRecord.amount)
        } else {
            updateDateOutByDateMinus(detailRecord.date, detailRecord.amount)
        }
        detailRecordRepository.delete(detailRecord)
        Log.d("DatabaseHelper", "向DetailRecord表中删除了数据")
    }

    suspend fun updateDetailRecord(
        oldDetailRecord: DetailRecord,
        newDetailRecord: DetailRecord
    ) {
        if (oldDetailRecord.isIncome && newDetailRecord.isIncome) {
            Log.d("DatabaseHelper", "更新详细记录导致的日收支更新，新旧记录都是收入")
            updateDateInByDateMinus(oldDetailRecord.date, oldDetailRecord.amount)
            updateDateInByDateAdd(newDetailRecord.date, newDetailRecord.amount)
        } else if (!oldDetailRecord.isIncome && !newDetailRecord.isIncome) {
            Log.d("DatabaseHelper", "更新详细记录导致的日收支更新，新旧记录都是支出")
            updateDateOutByDateMinus(oldDetailRecord.date, oldDetailRecord.amount)
            updateDateOutByDateAdd(newDetailRecord.date, newDetailRecord.amount)
        } else if (oldDetailRecord.isIncome && !newDetailRecord.isIncome) {
            Log.d("DatabaseHelper", "更新详细记录导致的日收支更新，旧记录是收入，新记录是支出")
            updateDateInByDateMinus(oldDetailRecord.date, oldDetailRecord.amount)
            updateDateOutByDateAdd(newDetailRecord.date, newDetailRecord.amount)
        } else if (!oldDetailRecord.isIncome && newDetailRecord.isIncome) {
            Log.d("DatabaseHelper", "更新详细记录导致的日收支更新，旧记录是支出，新记录是收入")
            updateDateOutByDateMinus(oldDetailRecord.date, oldDetailRecord.amount)
            updateDateInByDateAdd(newDetailRecord.date, newDetailRecord.amount)
        }

        detailRecordRepository.update(newDetailRecord)
        Log.d("DatabaseHelper", "向DetailRecord表中更新了旧数据")
    }
}