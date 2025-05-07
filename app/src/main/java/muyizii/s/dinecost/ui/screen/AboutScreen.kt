package muyizii.s.dinecost.ui.screen

import androidx.compose.foundation.Image
//import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
//import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
//import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import muyizii.s.dinecost.R

@Composable
fun AboutScreen(
    navController: NavHostController
) {
    val uriHandler = LocalUriHandler.current

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(85.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = "回退",
                modifier = Modifier
                    .padding(20.dp)
                    .clickable { navController.popBackStack() }
            )
            Text(
                modifier = Modifier,
                text = "关于",
                style = MaterialTheme.typography.titleLarge
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(0.90f),
                content = {
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(R.drawable.ic_hamburger),
                                contentDescription = "应用图标",
                                modifier = Modifier
                                    .size(128.dp)
                                    .padding(bottom = 16.dp)
                            )
                            Text(
                                text = "餐标",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                text = "Version 1.1.1",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.padding(20.dp))
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "开源地址：",
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            Text(
                                text = "https://github.com/Muyizii/Dinecost",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier
                                    .clickable
//                                        (
//                                        interactionSource = remember { MutableInteractionSource() },
//                                        indication = null
//                                    )
                                    {
                                        uriHandler.openUri("https://github.com/Muyizii/Dinecost")
                                    }
                            )
                        }
                        Spacer(modifier = Modifier.padding(8.dp))
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "更新地址：",
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            Text(
                                text = "https://github.com/Muyizii/Dinecost/releases",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier
                                    .clickable {
                                        uriHandler.openUri("https://github.com/Muyizii/Dinecost/releases")
                                    }
                            )
                        }
                        Spacer(modifier = Modifier.padding(8.dp))
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "问题反馈：",
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            Text(
                                text = "https://github.com/Muyizii/Dinecost/issues",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier
                                    .clickable {
                                        uriHandler.openUri("https://github.com/Muyizii/Dinecost/issues")
                                    }
                            )
                        }
                    }
                }
            )
        }
    }
}