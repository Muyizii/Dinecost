package muyizii.s.dinecost.viewModels

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import muyizii.s.dinecost.MainApplication

object AppViewModelProvider{
    val MVMFactory = viewModelFactory {
        initializer {
            MainViewModel(
                mainApplication().container.databaseHelper,
                mainApplication().container.notificationHelper,
                mainApplication().container.settingHelper
            )
        }
    }
    val SVMFactory = viewModelFactory {
        initializer {
            SettingViewModel(
                mainApplication().container.databaseHelper,
                mainApplication().container.settingHelper,
                mainApplication().container.notificationHelper,
                mainApplication().container.permissionHelper
            )
        }
    }
    val ATVMFactory = viewModelFactory {
        initializer {
            AddTypeViewModel(
                mainApplication().container.databaseHelper
            )
        }
    }
}

// 为[Application]对象创建扩展函数，用于查询并返回[InventoryApplication]实例
fun CreationExtras.mainApplication(): MainApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as MainApplication)