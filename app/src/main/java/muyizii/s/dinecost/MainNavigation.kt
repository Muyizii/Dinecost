package muyizii.s.dinecost

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import kotlinx.coroutines.launch
import muyizii.s.dinecost.ui.screen.AboutScreen
import muyizii.s.dinecost.ui.screen.AddDetailRecordScreen
import muyizii.s.dinecost.ui.screen.AddRecordTypeScreen
import muyizii.s.dinecost.ui.screen.AllDetailRecordListScreen
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
    refreshTrigger: Int = 0,
    mainViewModel: MainViewModel = viewModel(factory = AppViewModelProvider.MVMFactory),
    settingViewModel: SettingViewModel = viewModel(factory = AppViewModelProvider.SVMFactory),
) {
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()

    val pagerState = rememberPagerState { 3 }
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val bottomBarExcludedRoutes = setOf(
        Routes.SHOW_AUTO_RECORDER_MODE_SMS_INFO,
        Routes.SHOW_AUTO_RECORDER_MODE_A11Y_INFO,
        Routes.ABOUT
    )

    LaunchedEffect(refreshTrigger) {
        if (refreshTrigger > 1) {
            Log.d("MainNavigation", "调用了MainViewModel的页面数据获取方法")
            mainViewModel.generateCalendarDays()
            mainViewModel.generateTotalInOut()
            mainViewModel.generateDetailRecordList()
            mainViewModel.generateRecordTypeList()
        }
    }

    // 监听页面滑动，更新底部导航栏选中状态
    LaunchedEffect(pagerState.currentPage) {
        selectedTabIndex = pagerState.currentPage
    }

    Scaffold(
        bottomBar = {
            val navBackStackEntry = navController.currentBackStackEntryAsState().value
            val currentRoute = navBackStackEntry?.destination?.route

            if (currentRoute !in bottomBarExcludedRoutes) {
                BottomNavigationBar(
                    selectedTabIndex = selectedTabIndex,
                    onTabSelected = { index ->
                        // 当点击底部导航栏时，滑动到对应页面
                        if (currentRoute != Routes.MAIN) {
                            navController.navigate(Routes.MAIN)
                        }
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }
                )
            }
        },
        floatingActionButton = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            if (currentRoute == Routes.MAIN) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    when (pagerState.currentPage) {
                        0, 1 -> FloatingActionButton(
                            modifier = Modifier
                                .align(Alignment.BottomEnd),
                            onClick = { navController.navigate("ADD_DETAIL_RECORD_SCREEN") },
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_add),
                                contentDescription = "添加"
                            )
                        }
                    }
                }
            }
        },
    ) { innerPadding ->
        // 使用两层导航：外层是NavHost处理深层导航，内层是HorizontalPager处理主页面滑动
        NavHost(
            navController = navController,
            startDestination = Routes.MAIN,
            modifier = Modifier.padding(innerPadding)
        ) {
            // 主页面导肮
            composable(
                route = Routes.MAIN,
                deepLinks = listOf(
                    navDeepLink { uriPattern = "dine_cost://detail_record_list_screen" }
                )
            ) {
                HorizontalPager(
                    state = pagerState,
                ) { page ->
                    when (page) {
                        0 -> CalendarScreen(
                            mainViewModel = mainViewModel,
                            navController = navController
                        )

                        1 -> DetailRecordListScreen(
                            mainViewModel = mainViewModel,
                            navController = navController
                        )

                        2 -> SettingScreen(
                            settingViewModel = settingViewModel,
                            navController = navController
                        )
                    }
                }
            }
            // 其他页面
            composable(Routes.ABOUT) {
                AboutScreen(
                    navController = navController
                )
            }
            composable(Routes.ALL_DETAIL_RECORD_LIST) {
                AllDetailRecordListScreen(
                    mainViewModel = mainViewModel,
                    navController = navController
                )
            }
            composable(
                route = Routes.ADD_DETAIL_RECORD,
                deepLinks = listOf(
                    navDeepLink { uriPattern = "dine_cost://add_cash_screen" }
                )
            ) {
                AddDetailRecordScreen(
                    mainViewModel = mainViewModel,
                    navController = navController
                )
            }
            composable(Routes.ADD_RECORD_TYPE) {
                AddRecordTypeScreen(
                    mainViewModel = mainViewModel,
                    addTypeViewModel = viewModel(factory = AppViewModelProvider.ATVMFactory),
                    navController = navController
                )
            }
            composable(Routes.MANAGE_RECORD_TYPE) {
                ManageRecordTypeScreen(
                    mainViewModel = mainViewModel,
                    navController = navController
                )
            }
            composable(Routes.MANAGE_DETAIL_RECORD) {
                ManageDetailRecordScreen(
                    mainViewModel = mainViewModel,
                    navController = navController
                )
            }
            composable(Routes.UPDATE_DETAIL_RECORD) {
                UpdateDetailRecordScreen(
                    mainViewModel = mainViewModel,
                    navController = navController
                )
            }
            composable(Routes.SETTING) {
                SettingScreen(
                    settingViewModel = settingViewModel,
                    navController = navController
                )
            }
            composable(Routes.AUTO_RECORDER_MANAGE) {
                AutoRecorderManageScreen(
                    settingViewModel = settingViewModel,
                    navController = navController
                )
            }
            composable(Routes.SHOW_AUTO_RECORDER_MODE_A11Y_INFO) {
                ShowAutoRecorderModeA11yInfoScreen(
                    navController = navController
                )
            }
            composable(Routes.SHOW_AUTO_RECORDER_MODE_SMS_INFO) {
                ShowAutoRecorderModeSmsInfoScreen(
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = selectedTabIndex == 0,
            onClick = { onTabSelected(0) },
            label = { Text("日历") },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_calendar),
                    contentDescription = "日历"
                )
            }
        )
        NavigationBarItem(
            selected = selectedTabIndex == 1,
            onClick = { onTabSelected(1) },
            label = { Text("记录") },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_list),
                    contentDescription = "记录"
                )
            }
        )
        NavigationBarItem(
            selected = selectedTabIndex == 2,
            onClick = { onTabSelected(2) },
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

object Routes {
    const val MAIN: String = "MAIN_SCREEN"
    const val ABOUT: String = "ABOUT_SCREEN"
    const val CALENDAR: String = "CALENDAR_SCREEN"
    const val DETAIL_RECORD_LIST: String = "DETAIL_RECORD_LIST_SCREEN"
    const val ALL_DETAIL_RECORD_LIST: String = "ALL_DETAIL_RECORD_LIST_SCREEN"
    const val ADD_DETAIL_RECORD: String = "ADD_DETAIL_RECORD_SCREEN"
    const val ADD_RECORD_TYPE: String = "ADD_RECORD_TYPE_SCREEN"
    const val MANAGE_RECORD_TYPE: String = "MANAGE_RECORD_TYPE_SCREEN"
    const val MANAGE_DETAIL_RECORD: String = "MANAGE_DETAIL_RECORD_SCREEN"
    const val UPDATE_DETAIL_RECORD: String = "UPDATE_DETAIL_RECORD_SCREEN"
    const val SETTING: String = "SETTING_SCREEN"
    const val AUTO_RECORDER_MANAGE: String = "AUTO_RECORDER_MANAGE_SCREEN"
    const val SHOW_AUTO_RECORDER_MODE_A11Y_INFO: String = "SHOW_AUTO_RECORDER_MODE_A11Y_INFO_SCREEN"
    const val SHOW_AUTO_RECORDER_MODE_SMS_INFO: String = "SHOW_AUTO_RECORDER_MODE_SMS_INFO_SCREEN"
}