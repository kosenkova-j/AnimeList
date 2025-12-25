package com.example.animelist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.animelist.presentation.screen.home.HomeScreen
import com.example.animelist.presentation.theme.AnimeListTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnimeListTheme {
                // ТЕСТ: Просто покажите текст
                Text("Приложение запущено", modifier = Modifier.padding(16.dp))

                // ИЛИ: HomeScreen
                HomeScreen(
                    onAnimeClick = { id -> println("Clicked: $id") },
                    onSearchClick = { println("Search clicked") }
                )
            }
        }
    }
}