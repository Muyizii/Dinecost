package muyizii.s.dinecost.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import muyizii.s.dinecost.helpers.DatabaseHelper
import muyizii.s.dinecost.TypeUiState
import muyizii.s.dinecost.data.recordType.RecordType

typealias RT = RecordType

class AddTypeViewModel(
    private val databaseHelper: DatabaseHelper
) : ViewModel() {
    private val _uiState = MutableStateFlow(TypeUiState())
    val uiState: StateFlow<TypeUiState> = _uiState.asStateFlow()

    init {
        _uiState.update { currentState ->
            currentState.copy(
                chosenType = "饮食",
                titleTypeList = listOf(
                    "饮食", "交通", "住房", "医疗健康", "数字服务", "个人护理", "日用百货",
                    "娱乐休闲", "购物消费", "社交人情", "财务规划", "意外支出", "收入"
                ),
                typeListMap = mapOf(
                    "饮食" to listOf(
                        RT(name = "饮食"), RT(name = "外卖"), RT(name = "外食"), RT(name = "食材"),
                        RT(name = "零食"), RT(name = "饮料"), RT(name = "酒水"), RT(name = "香烟"),
                        RT(name = "奶茶咖啡"), RT(name = "甜品")
                    ),
                    "交通" to listOf(
                        RT(name = "交通"), RT(name = "公共交通"), RT(name = "出租车"), RT(name = "网约车"),
                        RT(name = "租车"), RT(name = "燃油"), RT(name = "停车费"), RT(name = "保养"),
                        RT(name = "过路费"), RT(name = "车辆保险")
                    ),
                    "住房" to listOf(
                        RT(name = "住房"), RT(name = "房租"), RT(name = "房贷"), RT(name = "物业费"),
                        RT(name = "水费"), RT(name = "电费"), RT(name = "燃气费"), RT(name = "网络费"),
                        RT(name = "家政服务"), RT(name = "维修费")
                    ),
                    "医疗健康" to listOf(
                        RT(name = "医疗健康"), RT(name = "医院"), RT(name = "诊所"), RT(name = "药品"),
                        RT(name = "医疗器械"), RT(name = "健康检查"), RT(name = "牙科护理"), RT(name = "健身"),
                        RT(name = "运动"), RT(name = "医疗保险")
                    ),
                    "数字服务" to listOf(
                        RT(name = "数字服务"), RT(name = "订阅服务"), RT(name = "通讯与连接"), RT(name = "云存储"),
                        RT(name = "工具与生产力"), RT(name = "电子游戏"), RT(name = "线上娱乐"), RT(name = "虚拟商品与服务"),
                        RT(name = "数字内容"), RT(name = "软件"), RT(name = "网络安全服务")
                    ),
                    "个人护理" to listOf(
                        RT(name = "个人护理"), RT(name = "理发"), RT(name = "美容"), RT(name = "护肤品"),
                        RT(name = "化妆品"), RT(name = "洗漱用品"), RT(name = "卫生用品")
                    ),
                    "日用百货" to listOf(
                        RT(name = "日用百货"), RT(name = "家居用品"), RT(name = "厨房用品"), RT(name = "清洁用品"),
                        RT(name = "卫浴用品"), RT(name = "家纺类"), RT(name = "小家电"), RT(name = "宠物用品"),
                        RT(name = "文具办公"), RT(name = "收纳用品"), RT(name = "其他杂项")
                    ),
                    "娱乐休闲" to listOf(
                        RT(name = "娱乐休闲"), RT(name = "电影"), RT(name = "演出"), RT(name = "主题公园"),
                        RT(name = "旅行"), RT(name = "度假"), RT(name = "酒店"), RT(name = "音乐会"),
                        RT(name = "展会"), RT(name = "聚会活动"), RT(name = "线下游戏")
                    ),
                    "购物消费" to listOf(
                        RT(name = "购物消费"), RT(name = "服装"), RT(name = "鞋帽"), RT(name = "家具家居"),
                        RT(name = "电子产品"), RT(name = "数码3C"), RT(name = "奢侈品"), RT(name = "装饰品"),
                        RT(name = "书籍杂志")
                    ),
                    "社交人情" to listOf(
                        RT(name = "社交人情"), RT(name = "朋友聚餐"), RT(name = "礼物"), RT(name = "婚庆礼金"),
                        RT(name = "红包"), RT(name = "捐赠"), RT(name = "慈善"), RT(name = "节日礼品")
                    ),
                    "财务规划" to listOf(
                        RT(name = "财务规划"), RT(name = "储蓄"), RT(name = "投资"), RT(name = "税务规划"),
                        RT(name = "教育"), RT(name = "自我提升"), RT(name = "转账支出")
                    ),
                    "意外支出" to listOf(
                        RT(name = "意外支出"), RT(name = "维修费用"), RT(name = "罚款"), RT(name = "医疗急救"),
                        RT(name = "赔偿"), RT(name = "意外损失"), RT(name = "自然灾害损失"), RT(name = "其它")
                    ),
                    "收入" to listOf(
                        RT(name = "收入", isIncome = true), RT(name = "工资薪水", isIncome = true),
                        RT(name = "副业收入", isIncome = true), RT(name = "奖金绩效", isIncome = true),
                        RT(name = "投资收益", isIncome = true), RT(name = "租金收入", isIncome = true),
                        RT(name = "版权收入", isIncome = true), RT(name = "补贴退税", isIncome = true),
                        RT(name = "红包礼物", isIncome = true), RT(name = "转卖收入", isIncome = true),
                        RT(name = "转账收入", isIncome = true), RT(name = "意外收入", isIncome = true)
                    )
                )
            )
        }
    }

    fun chooseAnotherTitle(title: String) {
        _uiState.update { currentState ->
            currentState.copy(
                chosenType = title
            )
        }
    }

    fun addRecordType(recordType: RecordType) {
        viewModelScope.launch {
            val isSuccess = databaseHelper.addRecordType(recordType)
            if (isSuccess) {
                _uiState.update { currentState ->
                    currentState.copy(
                        showSnackbar = 1
                    )
                }
                Log.d("AddTypeViewModel", "将showSnackbar赋值1")
            } else {
                _uiState.update { currentState ->
                    currentState.copy(
                        showSnackbar = 2
                    )
                }
                Log.d("AddTypeViewModel", "将showSnackbar赋值2")
            }
        }
    }

    fun resetSnackbar() {
        _uiState.update { currentState ->
            currentState.copy(showSnackbar = 0)
        }
        Log.d("AddTypeViewModel", "将showSnackbar赋值0")
    }
}