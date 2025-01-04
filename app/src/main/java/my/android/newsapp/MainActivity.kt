package my.android.newsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.tv.material3.Surface
import dagger.hilt.android.AndroidEntryPoint
import my.android.newsapp.ui.screen.HomeScreen
import my.android.newsapp.ui.theme.NewsAppTheme
import my.android.newsapp.ui.viewmodel.HomeViewmodel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val homeViewmodel :HomeViewmodel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = RectangleShape
                ) {
                    HomeScreen(homeViewmodel)
                }
            }
        }
    }
}