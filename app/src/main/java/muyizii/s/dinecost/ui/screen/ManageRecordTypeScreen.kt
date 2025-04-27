package muyizii.s.dinecost.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import muyizii.s.dinecost.viewModels.MainViewModel
import muyizii.s.dinecost.R

@Composable
fun ManageRecordTypeScreen(
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
                text = "管理记录的类型",
                style = MaterialTheme.typography.titleLarge
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(0.90f),
                content = {
                    item {
                        Text(
                            modifier = Modifier.padding(8.dp),
                            text = "收入类型"
                        )
                    }
                    items(mainUiState.recordTypeInList.size) { index ->
                        val type = mainUiState.recordTypeInList[index]
                        Surface(
                            modifier = Modifier
                                .padding(16.dp)
                                .height(72.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.primary,
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = type.name,
                                    modifier = Modifier.padding(16.dp)
                                )
                                OutlinedButton(
                                    onClick = {
                                        mainViewModel.deleteRecordType(type)
                                    },
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text(text = "删除", color = MaterialTheme.colorScheme.surfaceVariant)
                                }
                            }
                        }
                    }
                    item {
                        Text(
                            modifier = Modifier.padding(8.dp),
                            text = "支出类型"
                        )
                    }
                    items(mainUiState.recordTypeOutList.size) { index ->
                        val type = mainUiState.recordTypeOutList[index]
                        Surface(
                            modifier = Modifier
                                .padding(16.dp)
                                .height(72.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.secondary,
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = type.name,
                                    modifier = Modifier.padding(16.dp)
                                )
                                OutlinedButton(
                                    onClick = {
                                        mainViewModel.deleteRecordType(type)
                                    },
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text(text = "删除", color = MaterialTheme.colorScheme.surfaceVariant)
                                }
                            }
                        }
                    }
                }
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            onClick = { navController.navigate("ADD_RECORD_TYPE_SCREEN") }
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(modifier = Modifier.padding(8.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = "添加"
                )
                Text("  添加类型")
                Spacer(modifier = Modifier.padding(8.dp))
            }
        }
    }
}
