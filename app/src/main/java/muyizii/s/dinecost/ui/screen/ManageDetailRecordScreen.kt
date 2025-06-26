package muyizii.s.dinecost.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import muyizii.s.dinecost.Routes
import muyizii.s.dinecost.viewModels.MainViewModel

@Composable
fun ManageDetailRecordScreen(
    mainViewModel: MainViewModel,
    navController: NavHostController
) {
    val mainUiState by mainViewModel.uiState.collectAsState()

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
                text = "管理这项记录",
                style = MaterialTheme.typography.titleLarge
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(0.90f)
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(16.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = if (mainUiState.chosenDetailRecord.isIncome) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.secondary
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(0.85f)
                        ) {
                            Text(text = "日期")
                            Text(
                                text = mainUiState.chosenDetailRecord.date.toString()
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(0.85f)
                        ) {
                            Text(text = "金额")
                            Text(
                                text = mainUiState.chosenDetailRecord.amount.toString()
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(0.85f)
                        ) {
                            Text(text = "类型")
                            Text(text = mainUiState.chosenDetailRecord.type)
                        }
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(0.85f)
                        ) {
                            Text(text = "子类型")
                            Text(text = mainUiState.chosenDetailRecord.subType)
                        }
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(0.85f)
                        ) {
                            Text(text = "收支")
                            Text(
                                text = if (mainUiState.chosenDetailRecord.isIncome) "收入" else "支出"
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(0.85f)
                        ) {
                            Text(text = "备注")
                            Text(
                                text = mainUiState.chosenDetailRecord.detail
                            )
                        }
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            navController.navigate("UPDATE_DETAIL_RECORD_SCREEN")
                        },
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text("修改")
                    }

                    Button(
                        onClick = {
                            mainViewModel.deleteDetailRecord()
                            navController.navigate(Routes.MAIN)
                        },
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text("删除")
                    }
                }
            }
        }
    }
}