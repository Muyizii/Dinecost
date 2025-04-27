package muyizii.s.dinecost.ui.screen

import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import muyizii.s.dinecost.R
import muyizii.s.dinecost.viewModels.MainViewModel
import java.math.BigDecimal
import java.math.RoundingMode

@Composable
fun CalendarScreen(
    mainViewModel: MainViewModel,
    navController: NavHostController
) {
    val mainUiState by mainViewModel.uiState.collectAsState()

    var expanded by remember { mutableStateOf(false) }
    var parentWidth by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(85.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .clickable { expanded = true }
                    .onGloballyPositioned { coordinates ->
                        parentWidth = coordinates.size.width
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier.padding(20.dp),
                    text = mainUiState.chosenDate.year.toString() + "年" +
                            mainUiState.chosenDate.monthValue.toString() + "月",
                    style = MaterialTheme.typography.titleLarge
                )
                DropdownMenu(
                    modifier = Modifier.heightIn(max = 400.dp),
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    offset = DpOffset(
                        x = with(LocalDensity.current) { (parentWidth / 2).toDp() } - 100.dp,
                        y = 0.dp
                    )
                ) {
                    mainUiState.monthList.forEach { month ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    text = month.year.toString() + "年" + month.monthValue.toString() + "月"
                                )
                            },
                            onClick = {
                                mainViewModel.chooseAnotherMonth(month)
                                expanded = false
                            }
                        )
                    }
                }
            }
            Text(
                modifier = Modifier.padding(20.dp),
                text = "余额: " + BigDecimal(mainUiState.totalIn - mainUiState.totalOut)
                    .setScale(2, RoundingMode.HALF_UP)
                    .toDouble().toString(),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.padding(8.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            for (day in listOf<String>("一", "二", "三", "四", "五", "六", "日")) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = day, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }

        Spacer(modifier = Modifier.padding(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier
                .fillMaxWidth(),
            content = {
                items(mainUiState.calendarDays.size) { index ->
                    val day = mainUiState.calendarDays[index]
                    if (day.isCurrentMonth) {
                        Surface(
                            modifier =
                                if (day.date == mainUiState.nowDate) {
                                    Modifier
                                        .padding(2.dp)
                                        .height(64.dp)
                                        .border(
                                            width = 1.dp,
                                            color = MaterialTheme.colorScheme.primary,
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                } else {
                                    Modifier
                                        .padding(2.dp)
                                        .height(64.dp)
                                },
                            color =
                                if (day.date == mainUiState.chosenDate) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.background,
                            shape = RoundedCornerShape(12.dp),
                            onClick = {
                                mainViewModel.chooseAnotherDate(newChosenDate = day.date)
                            }
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    modifier = Modifier.padding(2.dp),
                                    text = day.date.dayOfMonth.toString()
                                )
                                Text(
                                    text =
                                        if (day.price == 0.0) ""
                                        else day.price.toString(),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    } else {
                        Surface(
                            modifier = Modifier
                                .padding(2.dp)
                                .height(60.dp),
                            color = MaterialTheme.colorScheme.background,
                            shape = RoundedCornerShape(10.dp)
                        ) {}
                    }
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            onClick = { navController.navigate("ADD_DETAIL_RECORD_SCREEN") },
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_add),
                contentDescription = "添加"
            )
        }
    }
}