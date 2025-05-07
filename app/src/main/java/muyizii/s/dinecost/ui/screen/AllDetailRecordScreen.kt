package muyizii.s.dinecost.ui.screen

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import muyizii.s.dinecost.viewModels.MainViewModel

@Composable
fun AllDetailRecordListScreen(
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
                text = "所有记录",
                style = MaterialTheme.typography.titleLarge
            )
        }
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            content = {
                items(mainUiState.allDetailRecordList.size) { index ->
                    val detailRecord = mainUiState.allDetailRecordList[index]
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .padding(6.dp)
                            .height(40.dp),
                        shape = RoundedCornerShape(12.dp),
                        color =
                            if (detailRecord.isPatch)
                                MaterialTheme.colorScheme.error
                            else if (detailRecord.isIncome)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.secondary,
                        onClick = {
                            mainViewModel.chooseAnotherDetailRecord(detailRecord.id)
                            navController.navigate("MANAGE_DETAIL_RECORD_SCREEN")
                        }
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                modifier = Modifier.padding(start = 8.dp),
                                text = if (detailRecord.isIncome) "收入" else "支出"
                            )
                            Text(text = detailRecord.amount.toString())
                            Text(
                                modifier = Modifier.padding(end = 8.dp),
                                text = detailRecord.type
                            )
                        }
                    }
                }
            }
        )
    }
}