package com.hello.contactapproom.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.hello.contactapproom.presentation.AddEditScreen
import com.hello.contactapproom.presentation.AllContactScreen
import com.hello.contactapproom.presentation.ContactState
import com.hello.contactapproom.presentation.ContactViewModel

@Composable
fun NavHostGraph(
    navController: NavHostController,
    state: ContactState,
    viewModel: ContactViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Routes.AllContactScreen.route
    ) {
        composable(Routes.AllContactScreen.route) {
            AllContactScreen(viewModel = viewModel, state = state, navController = navController)
        }

        composable(Routes.AddEditScreen.route) {
            AddEditScreen(state = state, navController = navController) {
                viewModel.saveContact()
            }
        }
    }
}