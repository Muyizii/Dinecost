package muyizii.s.dinecost

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import muyizii.s.dinecost.ui.theme.MuyiziisDinecostTheme

class MainActivity : ComponentActivity() {

    private val refreshTrigger = mutableStateOf(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MuyiziisDinecostTheme {
                val refreshState = remember { refreshTrigger }
                MainNavigation(
                    refreshTrigger = refreshState.value
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        refreshTrigger.value = refreshTrigger.value + 1
    }
}