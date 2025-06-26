package muyizii.s.dinecost.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import muyizii.s.dinecost.viewModels.MainViewModel

@Composable
fun DetailRecordListScreen(
    mainViewModel: MainViewModel,
    navController: NavHostController
) {
    val mainUiState by mainViewModel.uiState.collectAsState()

    // 使用 remember 保持防抖状态
    var lastEventTime by remember { mutableLongStateOf(0L) }
    val debounceInterval = 1000L // 防抖间隔

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(85.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.padding(20.dp),
                text = mainUiState.chosenDate.year.toString() + "年" +
                        mainUiState.chosenDate.monthValue.toString() + "月" +
                        mainUiState.chosenDate.dayOfMonth.toString() + "日",
                style = MaterialTheme.typography.titleLarge
            )
        }
        Row(
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                text = if (mainUiState.chosenDate == mainUiState.nowDate) {
                    if (mainUiState.detailRecordList.isNotEmpty())
                        "今日共有" + mainUiState.detailRecordList.size.toString() + "条记录"
                    else
                        "今日暂无记录"
                } else {
                    if (mainUiState.detailRecordList.isNotEmpty())
                        "该日共有" + mainUiState.detailRecordList.size.toString() + "条记录"
                    else
                        "该日暂无记录"
                },
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = "（点此查看所有记录）",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.clickable {
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastEventTime >= debounceInterval) {
                        lastEventTime = currentTime
                        mainViewModel.generateAllDetailRecordList {
                            navController.navigate("ALL_DETAIL_RECORD_LIST_SCREEN")
                        }
                    }
                }
            )
        }
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            content = {
                items(mainUiState.detailRecordList.size) { index ->
                    val detailRecord = mainUiState.detailRecordList[index]
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .padding(10.dp)
                            .height(100.dp),
                        shape = RoundedCornerShape(12.dp),
                        color =
                            if (detailRecord.isPatch)
                                MaterialTheme.colorScheme.error
                            else if (detailRecord.isIncome)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.secondary,
                        onClick = {
                            val currentTime = System.currentTimeMillis()
                            if (currentTime - lastEventTime >= debounceInterval) {
                                lastEventTime = currentTime
                                mainViewModel.chooseAnotherDetailRecord(detailRecord.id)
                                navController.navigate("MANAGE_DETAIL_RECORD_SCREEN")
                            }
                        }
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(text = detailRecord.amount.toString())
                            Text(text = detailRecord.type)
                            Text(text = if (detailRecord.isIncome) "收入" else "支出")
                        }
                    }
                }
            }
        )
    }
}