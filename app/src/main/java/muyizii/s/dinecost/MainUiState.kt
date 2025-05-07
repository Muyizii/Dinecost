package muyizii.s.dinecost

import muyizii.s.dinecost.data.detailRecord.DetailRecord
import muyizii.s.dinecost.data.recordType.RecordType
import java.time.LocalDate

// 主要的UI数据类
data class MainUiState(

    val isFirstRun: Boolean = false,
    val chosenDate: LocalDate = LocalDate.of(1, 1, 1),   // 用户所选择的日期
    val nowDate: LocalDate = LocalDate.of(1, 1, 1),       // 今日日期
    val chosenDetailRecord: DetailRecord = DetailRecord(
        id = -1,
        date = LocalDate.of(1, 1, 1),
        amount = 0.0,
        isIncome = false,
        isPatch = false,
        type = "",
        subType = "",
        detail = ""
    ),

    val totalIn: Double = 0.0,
    val totalOut: Double = 0.0,
    val todayOut: Double = 0.0,

    val monthList: List<LocalDate> = emptyList(),
    val calendarDays: List<CalendarDays> = emptyList(),  // 提供日历界面所需的数据
    val detailRecordList: List<DetailRecord> = emptyList(), // 提供详细记录界面所需的数据
    val allDetailRecordList: List<DetailRecord> = emptyList(), //所有的详细记录
    val recordTypeInList: List<RecordType> = emptyList(),
    val recordTypeOutList: List<RecordType> = emptyList()
)

// 设置界面的UI数据类（需要注意的是不包含设置的状态）
data class SettingUiState(
    val notificationPermissionDisabled: Boolean = false,
    val notificationIsDisabled: Boolean = false,
    val onGoingNotificationChannelDisabled: Boolean = false
)

data class TypeUiState(
    val showSnackbar: Int = 0,
    val titleTypeList: List<String> = emptyList(),
    val typeListMap: Map<String, List<RecordType>> = emptyMap(),
    val chosenType: String = ""
)

// 设置的状态
data class SettingState(
    val onGoingNotificationEnabled: Boolean = false,
    val autoRecorderMode: String = "None"
)

// 日历界面中格子所需要的数据
data class CalendarDays(
    val date: LocalDate,
    val price: Double,
    val isCurrentMonth: Boolean,
    val isToday: Boolean,
    val hasPatch: Boolean = false
)