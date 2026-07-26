package com.andikas.wang

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.andikas.wang.ui.navigation.NavGraph
import com.andikas.wang.ui.theme.WangTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WangTheme {
                NavGraph()
            }
        }
    }
}