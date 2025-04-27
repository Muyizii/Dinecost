package muyizii.s.dinecost

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import muyizii.s.dinecost.ui.screen.AboutScreen
import muyizii.s.dinecost.ui.screen.AddDetailRecordScreen
import muyizii.s.dinecost.ui.screen.AddRecordTypeScreen
import muyizii.s.dinecost.ui.screen.AutoRecorderManageScreen
import muyizii.s.dinecost.ui.screen.CalendarScreen
import muyizii.s.dinecost.ui.screen.DetailRecordListScreen
import muyizii.s.dinecost.ui.screen.ManageDetailRecordScreen
import muyizii.s.dinecost.ui.screen.ManageRecordTypeScreen
import muyizii.s.dinecost.ui.screen.SettingScreen
import muyizii.s.dinecost.ui.screen.ShowAutoRecorderModeA11yInfoScreen
import muyizii.s.dinecost.ui.screen.ShowAutoRecorderModeSmsInfoScreen
import muyizii.s.dinecost.ui.screen.UpdateDetailRecordScreen
import muyizii.s.dinecost.viewModels.AppViewModelProvider
import muyizii.s.dinecost.viewModels.MainViewModel
import muyizii.s.dinecost.viewModels.SettingViewModel

@Composable
fun MainNavigation(
    mainViewModel: MainViewModel = viewModel(factory = AppViewModelProvider.MVMFactory),
    settingViewModel: SettingViewModel = viewModel(factory = AppViewModelProvider.SVMFactory),
) {
    val navController = rememberNavController()

    val bottomBarExcludedRoutes = setOf(
        "SHOW_AUTO_RECORDER_MODE_A11Y_INFO_SCREEN",
        "SHOW_AUTO_RECORDER_MODE_SMS_INFO_SCREEN",
        "ABOUT_SCREEN"
    )

    Scaffold(
        bottomBar = {
            val navBackStackEntry = navController.currentBackStackEntryAsState().value
            val currentRoute = navBackStackEntry?.destination?.route

            if (currentRoute !in bottomBarExcludedRoutes) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "CALENDAR_SCREEN",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("ABOUT_SCREEN") {
                AboutScreen(
                    navController = navController
                )
            }
            composable("CALENDAR_SCREEN") {
                CalendarScreen(
                    mainViewModel = mainViewModel,
                    navController = navController
                )
            }
            composable(
                route = "DETAIL_RECORD_LIST_SCREEN",
                deepLinks = listOf(
                    navDeepLink { uriPattern = "dine_cost://detail_record_list_screen" }
                )
            ) {
                DetailRecordListScreen(
                    mainViewModel = mainViewModel,
                    navController = navController
                )
            }
            composable(
                route = "ADD_DETAIL_RECORD_SCREEN",
                deepLinks = listOf(
                    navDeepLink { uriPattern = "dine_cost://add_cash_screen" }
                )
            ) {
                AddDetailRecordScreen(
                    mainViewModel = mainViewModel,
                    navController = navController
                )
            }
            composable("ADD_RECORD_TYPE_SCREEN") {
                AddRecordTypeScreen(
                    mainViewModel = mainViewModel,
                    addTypeViewModel = viewModel(factory = AppViewModelProvider.ATVMFactory),
                    navController = navController
                )
            }
            composable("MANAGE_RECORD_TYPE_SCREEN") {
                ManageRecordTypeScreen(
                    mainViewModel = mainViewModel,
                    navController = navController
                )
            }
            composable("MANAGE_DETAIL_RECORD_SCREEN") {
                ManageDetailRecordScreen(
                    mainViewModel = mainViewModel,
                    navController = navController
                )
            }
            composable("UPDATE_DETAIL_RECORD_SCREEN") {
                UpdateDetailRecordScreen(
                    mainViewModel = mainViewModel,
                    navController = navController
                )
            }
            composable("SETTING_SCREEN") {
                SettingScreen(
                    settingViewModel = settingViewModel,
                    navController = navController
                )
            }
            composable("AUTO_RECORDER_MANAGE_SCREEN") {
                AutoRecorderManageScreen(
                    settingViewModel = settingViewModel,
                    navController = navController
                )
            }
            composable("SHOW_AUTO_RECORDER_MODE_A11Y_INFO_SCREEN") {
                ShowAutoRecorderModeA11yInfoScreen(
                    navController = navController
                )
            }
            composable("SHOW_AUTO_RECORDER_MODE_SMS_INFO_SCREEN") {
                ShowAutoRecorderModeSmsInfoScreen(
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar {
        NavigationBarItem(
            selected = currentRoute == "CALENDAR_SCREEN",
            onClick = { navController.navigate("CALENDAR_SCREEN") },
            label = { Text("日历") },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_calendar),
                    contentDescription = "日历"
                )
            }
        )
        NavigationBarItem(
            selected = currentRoute == "DETAIL_RECORD_LIST_SCREEN",
            onClick = { navController.navigate("DETAIL_RECORD_LIST_SCREEN") },
            label = { Text("记录") },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_list),
                    contentDescription = "记录"
                )
            }
        )
        NavigationBarItem(
            selected = currentRoute == "SETTING_SCREEN",
            onClick = { navController.navigate("SETTING_SCREEN") },
            label = { Text("设置") },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_setting),
                    contentDescription = "设置"
                )
            }
        )
    }
}