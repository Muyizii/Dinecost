package muyizii.s.dinecost.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import muyizii.s.dinecost.viewModels.MainViewModel
import muyizii.s.dinecost.R
import muyizii.s.dinecost.Routes

@Composable
fun UpdateDetailRecordScreen(
    mainViewModel: MainViewModel,
    navController: NavHostController
) {
    val mainUiState by mainViewModel.uiState.collectAsState()

    var amount by remember { mutableStateOf(mainUiState.chosenDetailRecord.amount.toString()) }
    var type by remember { mutableStateOf(mainUiState.chosenDetailRecord.type) }
    var subType by remember { mutableStateOf(mainUiState.chosenDetailRecord.subType) }
    var detail by remember { mutableStateOf(mainUiState.chosenDetailRecord.detail) }
    var isIncome by remember { mutableStateOf(mainUiState.chosenDetailRecord.isIncome) }
    var showErrorAmount by remember { mutableStateOf(false) }
    var showErrorType by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var subExpanded by remember { mutableStateOf(false) }

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
                text = "修改这项记录",
                style = MaterialTheme.typography.titleLarge
            )
        }

        Spacer(modifier = Modifier.padding(8.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(0.90f)
            ) {
                OutlinedTextField(
                    value = amount,
                    onValueChange = {
                        amount = it
                        showErrorAmount = false
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    label = { Text("金额") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    ),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_yen),
                            contentDescription = "元"
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = showErrorAmount
                )

                Spacer(modifier = Modifier.padding(8.dp))

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = type,
                        onValueChange = {
                            type = it
                            showErrorType = false
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("类型") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                            unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                            disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        ),
                        trailingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow_drop_down),
                                contentDescription = "下拉",
                                modifier = Modifier.clickable { expanded = !expanded }
                            )
                        },
                        singleLine = true,
                        isError = showErrorType,
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .heightIn(max = 400.dp)
                    ) {
                        if (isIncome) {
                            mainUiState.recordTypeInList.forEach { aType ->
                                DropdownMenuItem(
                                    text = { Text(aType.name) },
                                    onClick = {
                                        type = aType.name
                                        expanded = false
                                    }
                                )
                            }
                        } else {
                            mainUiState.recordTypeOutList.forEach { aType ->
                                DropdownMenuItem(
                                    text = { Text(aType.name) },
                                    onClick = {
                                        type = aType.name
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.padding(8.dp))

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = subType,
                        onValueChange = {
                            subType = it
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("子类型") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                            unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                            disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        ),
                        trailingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow_drop_down),
                                contentDescription = "下拉",
                                modifier = Modifier.clickable { subExpanded = !subExpanded }
                            )
                        },
                        singleLine = true
                    )
                    DropdownMenu(
                        expanded = subExpanded,
                        onDismissRequest = { subExpanded = false },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .heightIn(max = 400.dp)
                    ) {
                        if (isIncome) {
                            mainUiState.recordTypeInList.forEach { aType ->
                                DropdownMenuItem(
                                    text = { Text(aType.name) },
                                    onClick = {
                                        subType = aType.name
                                        subExpanded = false
                                    }
                                )
                            }
                        } else {
                            mainUiState.recordTypeOutList.forEach { aType ->
                                DropdownMenuItem(
                                    text = { Text(aType.name) },
                                    onClick = {
                                        subType = aType.name
                                        subExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.padding(8.dp))

                OutlinedTextField(
                    value = detail,
                    onValueChange = {
                        detail = it
                        showErrorType = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("备注") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.padding(8.dp))

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
                            val isValid = amount.isNotBlank() && type.isNotBlank() && amount.isDouble()
                            showErrorAmount = amount.isBlank() || !amount.isDouble()
                            showErrorType = type.isBlank()
                            if (isValid) {
                                mainViewModel.updateDetailRecord(
                                    newAmount = amount,
                                    newType = type,
                                    newSubType = subType,
                                    newIsIncome = isIncome,
                                    newDetail = detail
                                )
                                navController.navigate(Routes.MAIN)
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