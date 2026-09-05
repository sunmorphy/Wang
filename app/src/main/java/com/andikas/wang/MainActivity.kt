package com.andikas.wang

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.lyricist.ProvideStrings
import cafe.adriel.lyricist.rememberStrings
import com.andikas.wang.data.local.PreferenceManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.andikas.wang.ui.components.LocalToastHostState
import com.andikas.wang.ui.components.ToastHost
import com.andikas.wang.ui.components.rememberToastHostState
import com.andikas.wang.ui.navigation.NavGraph
import com.andikas.wang.ui.theme.WangTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val preferenceManager: PreferenceManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val selectedLanguage by preferenceManager.selectedLanguage.collectAsState(initial = "en")
            val selectedTheme by preferenceManager.theme.collectAsState(initial = "SYSTEM")
            val lyricist = rememberStrings(
                defaultLanguageTag = "en",
                currentLanguageTag = selectedLanguage
            )

            ProvideStrings(lyricist) {
                val toastHostState = rememberToastHostState()
                CompositionLocalProvider(LocalToastHostState provides toastHostState) {
                    WangTheme(theme = selectedTheme) {
                        Box(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            NavGraph()
                            ToastHost(hostState = toastHostState)
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val permissionManager = com.andikas.wang.ui.utils.PermissionManager(this)
        if (permissionManager.hasNotificationListenerPermission() && !com.andikas.wang.service.FinancialNotificationListener.isConnected) {
            com.andikas.wang.service.FinancialNotificationListener.requestRebindService(this)
        }
    }
}