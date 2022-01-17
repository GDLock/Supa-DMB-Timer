package com.example.superdmbtimer.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.superdmbtimer.R
import com.example.superdmbtimer.navigation.NavigationManager
import com.example.superdmbtimer.ui.theme.SuperDMBTimerTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var navigationManager: NavigationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        setContent {
            val state by viewModel.state.collectAsState()

            SuperDMBTimerTheme(state.theme) {
                val systemUiController = rememberSystemUiController()
                val background = MaterialTheme.colors.surface
                val isLight = MaterialTheme.colors.isLight

                SideEffect {
                    systemUiController.setSystemBarsColor(
                        background,
                        isLight
                    )
                }
                if (state.isLoaded)
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = background
                    ) {
                        AnimatedNavGraph(navigationManager, state.startRoute)
                    }
                else Box(
                    Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background)
                ) {
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center),
                        contentScale = ContentScale.FillWidth,
                        painter = painterResource(id = R.drawable.man),
                        contentDescription = null
                    )
                }
            }
        }
    }
}