package com.hello.contactapproom.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.hello.contactapproom.navigation.NavHostGraph
import com.hello.contactapproom.ui.theme.ContactAppRoomTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel = hiltViewModel<ContactViewModel>()
            val state by viewModel.state.collectAsState()
            val navController = rememberNavController()
            ContactAppRoomTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    NavHostGraph(
                        navController = navController,
                        state = state,
                        viewModel = viewModel
                    )
                }

            }
        }
    }
}
