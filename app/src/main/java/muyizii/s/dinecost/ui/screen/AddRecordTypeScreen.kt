package muyizii.s.dinecost.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import muyizii.s.dinecost.R
import muyizii.s.dinecost.data.recordType.RecordType
import muyizii.s.dinecost.viewModels.AddTypeViewModel
import muyizii.s.dinecost.viewModels.MainViewModel

@Composable
fun AddRecordTypeScreen(
    mainViewModel: MainViewModel,
    addTypeViewModel: AddTypeViewModel,
    navController: NavHostController
) {
    val typeUiState by addTypeViewModel.uiState.collectAsState()

    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    var openDialog by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    // 监听 showSnackbar 变化
    LaunchedEffect(typeUiState.showSnackbar) {
        when (typeUiState.showSnackbar) {
            1 -> {
                mainViewModel.generateRecordTypeList()
                snackbarHostState.showSnackbar("类型添加成功")
                Log.d("AddRecordTypeScreen", "发送Snackbar：类型添加成功")
                addTypeViewModel.resetSnackbar()
            }

            2 -> {
                mainViewModel.generateRecordTypeList()
                snackbarHostState.showSnackbar("该类型已添加，请勿重复操作")
                Log.d("AddRecordTypeScreen", "发送Snackbar：类型添加失败")
                addTypeViewModel.resetSnackbar()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
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
                    text = "添加预设的类型或自定义类型",
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Row(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier.width(screenWidth / 5),
                    content = {
                        items(typeUiState.titleTypeList.size) { index ->
                            val title = typeUiState.titleTypeList[index]
                            Surface(
                                modifier = Modifier
                                    .width(screenWidth / 5)
                                    .height(screenWidth / 5)
                                    .padding(2.dp),
                                color = if (title == typeUiState.chosenType) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.background,
                                shape = RoundedCornerShape(12.dp),
                                onClick = { addTypeViewModel.chooseAnotherTitle(title) }
                            ) {
                                Text(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .wrapContentSize(Alignment.Center),
                                    textAlign = TextAlign.Center,
                                    text = title
                                )
                            }
                            if (index != typeUiState.titleTypeList.lastIndex) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(4.dp),
                                    thickness = 1.dp,
                                    color = MaterialTheme.colorScheme.surfaceVariant
                                )
                            }
                        }
                    }
                )
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier.width(screenWidth / 5 * 4),
                    content = {
                        items(typeUiState.typeListMap[typeUiState.chosenType]!!.size) { index ->
                            val type = typeUiState.typeListMap[typeUiState.chosenType]!![index]
                            Surface(
                                modifier = Modifier
                                    .width(screenWidth / 5)
                                    .height(screenWidth / 5)
                                    .padding(2.dp),
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                onClick = { addTypeViewModel.addRecordType(type) }
                            ) {
                                Text(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .wrapContentSize(Alignment.Center),
                                    textAlign = TextAlign.Center,
                                    text = type.name
                                )
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
                onClick = { openDialog = true }
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Spacer(modifier = Modifier.padding(8.dp))
                    Icon(Icons.Filled.Add, "浮动按钮")
                    Text("  添加自定义类型")
                    Spacer(modifier = Modifier.padding(8.dp))
                }
            }
        }
    }

    if (openDialog) {
        AddRecordTypeDialog(
            onDismissRequest = { openDialog = false },
            addTypeViewModel = addTypeViewModel
        )
    }
}


@Composable
fun AddRecordTypeDialog(
    onDismissRequest: () -> Unit,
    addTypeViewModel: AddTypeViewModel
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .height(280.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            var name by remember { mutableStateOf("") }
            var isIncome by remember { mutableStateOf(false) }
            var showErrorName by remember { mutableStateOf(false) }

            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    modifier = Modifier.padding(25.dp),
                    text = "添加记录类型",
                    style = MaterialTheme.typography.titleLarge
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(0.90f)
                    ) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = {
                                name = it
                                showErrorName = false
                            },
                            label = { Text("类型") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            isError = showErrorName
                        )
                        Spacer(modifier = Modifier.padding(16.dp))
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            FilterChip(
                                onClick = { isIncome = !isIncome },
                                label = {
                                    Text("收入")
                                },
                                selected = isIncome,
                                leadingIcon = if (isIncome) {
                                    {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_check),
                                            contentDescription = "打勾",
                                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                                        )
                                    }
                                } else {
                                    null
                                }
                            )
                            Button(
                                onClick = {
                                    val isValid = name.isNotBlank()
                                    showErrorName = name.isBlank()
                                    if (isValid) {
                                        addTypeViewModel.addRecordType(
                                            RecordType(
                                                name = name,
                                                isIncome = isIncome
                                            )
                                        )
                                        onDismissRequest()
                                    }
                                }
                            ) {
                                Text("提交")
                            }
                        }
                    }
                }
            }
        }
    }
}