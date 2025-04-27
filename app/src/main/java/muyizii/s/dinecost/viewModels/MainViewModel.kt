package muyizii.s.dinecost.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import muyizii.s.dinecost.CalendarDays
import muyizii.s.dinecost.helpers.DatabaseHelper
import muyizii.s.dinecost.MainUiState
import muyizii.s.dinecost.helpers.NotificationHelper
import muyizii.s.dinecost.helpers.SettingHelper
import muyizii.s.dinecost.SettingState
import muyizii.s.dinecost.data.detailRecord.DetailRecord
import muyizii.s.dinecost.data.recordType.RecordType
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

class MainViewModel(
    private val databaseHelper: DatabaseHelper,
    private val notificationHelper: NotificationHelper,
    private val settingHelper: SettingHelper
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    val settingState: StateFlow<SettingState> = settingHelper.settingState

    val totalInFlow = databaseHelper.getFirstDateInFlow()
    val totalOutFlow = databaseHelper.getFirstDateOutFlow()

    init {
        viewModelScope.launch {
            if (checkFirstRun()) {
                val firstDate = LocalDate.of(LocalDate.now().year, LocalDate.now().monthValue, 1)

                databaseHelper.initFirstDateIn(date = firstDate.minusMonths(1))
                databaseHelper.initFirstDateOut(date = firstDate.minusMonths(1))

                val today = LocalDate.now()

                _uiState.update { currentState ->
                    currentState.copy(
                        chosenDate = today,
                        nowDate = today
                    )
                }
                generateMonths()
                generateCalendarDays()
                generateDetailRecordList()
                generateRecordTypeList()
                generateTotalInOut()

                Log.d("MainViewModel", "首次运行")
                Log.d("MainViewModel", "MainViewModel已被初始化")
            } else {
                val totalIn = databaseHelper.getFirstDateIn()!!.amount.roundToDecimal(2)
                val totalOut = databaseHelper.getFirstDateOut()!!.amount.roundToDecimal(2)

                val today = LocalDate.now()

                _uiState.update { currentState ->
                    currentState.copy(
                        totalIn = totalIn,
                        totalOut = totalOut,
                        chosenDate = today,
                        nowDate = today
                    )
                }
                generateMonths()
                generateCalendarDays()
                generateDetailRecordList()
                generateRecordTypeList()
                generateTotalInOut()

                Log.d("MainViewModel", "非首次运行")
                Log.d("MainViewModel", "MainViewModel已被初始化")
            }

            settingState
                .map { it.onGoingNotificationEnabled }
                .distinctUntilChanged()
                .collect { isEnabled ->
                    if (isEnabled) {
                        sendOngoingNotification()
                    }
                }


            totalInFlow
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = null
                )
                .collect { totalIn ->
                if (totalIn != null) {
                    _uiState.update { currentState ->
                        currentState.copy(
                            totalIn = totalIn.amount.roundToDecimal(2)
                        )
                    }
                    sendOngoingNotification()
                    Log.d("MainViewModel", "总收入流接受到了新的有效数字")
                }
            }

            totalOutFlow
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = null
                )
                .collect { totalOut ->
                    if (totalOut != null) {
                        val todayOut = databaseHelper.getDateOutByDate(LocalDate.now())
                        _uiState.update { currentState ->
                            currentState.copy(
                                totalOut = totalOut.amount.roundToDecimal(2),
                                todayOut = todayOut?.amount ?: 0.0
                            )
                        }

                        sendOngoingNotification()
                        Log.d("MainViewModel", "总支出流接受到了新的有效数字")
                    }
                }
        }
    }

    // 小数处理，保留指定位数
    private fun Double.roundToDecimal(decimals: Int): Double {
        return BigDecimal(this).setScale(decimals, RoundingMode.HALF_UP).toDouble()
    }

    // 检查是否是第一次运行
    private suspend fun checkFirstRun(): Boolean {
        val firstDateIn = databaseHelper.getFirstDateIn()
        _uiState.update { currentState ->
            currentState.copy(
                isFirstRun = (firstDateIn == null)
            )
        }
        return (firstDateIn == null)
    }

    // 发送余额通知
    private fun sendOngoingNotification() {
        if (settingState.value.onGoingNotificationEnabled) {
            notificationHelper.sendOngoingNotification(
                todaySpend = (uiState.value.todayOut).roundToDecimal(2).toString(),
                totalHave = (uiState.value.totalIn - uiState.value.totalOut).roundToDecimal(2).toString()
            )
            Log.d("MainViewModel", "发送持久通知")
        }
    }

    // 生成日历页面所需的数据
    fun generateCalendarDays() {
        viewModelScope.launch {
            val today = uiState.value.chosenDate

            val firstDayOfMonth = today.withDayOfMonth(1)
            val startDate = firstDayOfMonth.minusDays(firstDayOfMonth.dayOfWeek.value.toLong() - 1)
            val lastDayOfMonth = today.withDayOfMonth(today.month.length(today.isLeapYear))
            val endDate = lastDayOfMonth.plusDays(7 - lastDayOfMonth.dayOfWeek.value.toLong())

            val calendarDays: MutableList<CalendarDays> = mutableListOf()

            var iDate = startDate
            while (iDate <= endDate) {
                val tempPrice = databaseHelper.getDateOutByDate(iDate)
                calendarDays.add(
                    CalendarDays(
                        date = iDate,
                        price = if (tempPrice != null) tempPrice.amount.roundToDecimal(2) else 0.0,
                        isCurrentMonth = iDate.month == uiState.value.chosenDate.month,
                        isToday = iDate == uiState.value.nowDate
                    )
                )
                iDate = iDate.plusDays(1)
            }

            Log.d("MainViewModel", "生成了" + calendarDays.size + "个日期数据")

            _uiState.update { currentState ->
                currentState.copy(
                    calendarDays = calendarDays
                )
            }
        }
    }

    // 生成详细页面所需的数据
    fun generateDetailRecordList() {
        viewModelScope.launch {
            val detailRecordList: List<DetailRecord> =
                databaseHelper.getDetailRecordByDate(uiState.value.chosenDate)
            _uiState.update { currentState ->
                currentState.copy(
                    detailRecordList = detailRecordList
                )
            }
        }
    }

    // 生成记录类型页面所需的数据
    fun generateRecordTypeList() {
        viewModelScope.launch {
            val recordTypeList: List<RecordType> = databaseHelper.getAllRecordType()
            val recordTypeInList: MutableList<RecordType> = mutableListOf()
            val recordTypeOutList: MutableList<RecordType> = mutableListOf()

            for (i in recordTypeList) {
                if (i.isIncome) {
                    recordTypeInList.add(i)
                } else {
                    recordTypeOutList.add(i)
                }
            }

            _uiState.update { currentState ->
                currentState.copy(
                    recordTypeOutList = recordTypeOutList,
                    recordTypeInList = recordTypeInList
                )
            }
        }
    }

    // 生成新的总记录
    fun generateTotalInOut() {
        viewModelScope.launch {
            val newTotalIn = databaseHelper.getFirstDateIn()!!.amount
            val newTotalOut = databaseHelper.getFirstDateOut()!!.amount
            val todayOutRecord = databaseHelper.getDateOutByDate(LocalDate.now())
            val newTodayOut = if (todayOutRecord == null) 0.0 else todayOutRecord.amount

            _uiState.update { currentState ->
                currentState.copy(
                    totalIn = newTotalIn.roundToDecimal(2),
                    totalOut = newTotalOut.roundToDecimal(2),
                    todayOut = newTodayOut.roundToDecimal(2)
                )
            }
            sendOngoingNotification()
        }
    }

    // 生成用户切换的月份
    fun generateMonths() {
        viewModelScope.launch {
            val startMonth = databaseHelper.getFirstDateIn()!!.date
            val nowMonth = LocalDate.of(LocalDate.now().year, LocalDate.now().monthValue, 1)
            val monthList: MutableList<LocalDate> = mutableListOf()
            var iMonth = nowMonth

            while (iMonth > startMonth) {
                monthList.add(iMonth)
                iMonth = iMonth.minusMonths(1)
            }
            _uiState.update { currentState ->
                currentState.copy(
                    monthList = monthList
                )
            }
        }
    }

    // 用户在月内切换日期
    fun chooseAnotherDate(newChosenDate: LocalDate) {
        _uiState.update { currentState ->
            currentState.copy(
                chosenDate = newChosenDate
            )
        }
        Log.d("MainViewModel", "用户选择的日期被切换为" + uiState.value.chosenDate)
        generateDetailRecordList()
    }

    // 用户在月外切换日期
    fun chooseAnotherMonth(newChosenDate: LocalDate) {
        _uiState.update { currentState ->
            currentState.copy(
                chosenDate = newChosenDate
            )
        }
        generateCalendarDays()
        generateDetailRecordList()
    }

    // 用户选择了一项记录
    fun chooseAnotherDetailRecord(id: Int) {
        viewModelScope.launch {
            val newChosenDetailRecord = databaseHelper.getDetailRecordById(id)!!
            _uiState.update { currentState ->
                currentState.copy(
                    chosenDetailRecord = newChosenDetailRecord
                )
            }
        }
    }

    // 数据库操作：添加一个新的详细记录
    fun addDetailRecord(amount: String, isIncome: Boolean, type: String, subType: String, detail: String) {
        viewModelScope.launch {
            databaseHelper.addDetailRecord(
                detailRecord = DetailRecord(
                    amount = amount.toDouble().roundToDecimal(2),
                    isIncome = isIncome,
                    type = type,
                    subType = subType,
                    isPatch = false,
                    date = uiState.value.chosenDate,
                    detail = detail
                )
            )
            generateCalendarDays()
            generateDetailRecordList()
            generateTotalInOut()
        }
    }

    // 数据库操作：删除一个详细记录
    fun deleteDetailRecord() {
        viewModelScope.launch {
            databaseHelper.deleteDetailRecord(
                detailRecord = uiState.value.chosenDetailRecord
            )
            generateCalendarDays()
            generateDetailRecordList()
            generateTotalInOut()
        }
    }

    // 数据库操作：更新一个详细记录
    fun updateDetailRecord(
        newAmount: String, newType: String, newSubType: String = "", newIsIncome: Boolean, newDetail: String
    ) {
        viewModelScope.launch {
            databaseHelper.updateDetailRecord(
                oldDetailRecord = uiState.value.chosenDetailRecord,
                newDetailRecord = DetailRecord(
                    id = uiState.value.chosenDetailRecord.id,
                    date = uiState.value.chosenDetailRecord.date,
                    amount = newAmount.toDouble().roundToDecimal(2),
                    isIncome = newIsIncome,
                    type = newType,
                    subType = newSubType,
                    detail = newDetail,
                    isPatch = false
                )
            )
            generateCalendarDays()
            generateDetailRecordList()
            generateTotalInOut()
            Log.d("MainViewModel", "调用的更新详细记录的方法")
        }
    }

    // 数据库操作：删除一个记录类型
    fun deleteRecordType(recordType: RecordType) {
        viewModelScope.launch {
            databaseHelper.deleteRecordType(
                recordType
            )
            Log.d(
                "MainViewModel", "在RecordType表中删除了id" + recordType.id + "名为" + recordType.name + "的类型"
            )
            generateRecordTypeList()
        }
    }
}