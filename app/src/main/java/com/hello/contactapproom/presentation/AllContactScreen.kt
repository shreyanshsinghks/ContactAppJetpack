package com.hello.contactapproom.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hello.contactapproom.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllContactScreen(
    viewModel: ContactViewModel,
    state: ContactState,
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(modifier = Modifier.clickable {
                viewModel.changeSorting()
            }, title = {
                Icon(imageVector = Icons.Rounded.Menu, contentDescription = null)
            })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Routes.AddEditScreen.route) }) {
                Icon(imageVector = Icons.Rounded.Add, contentDescription = null)
            }
        }
    ) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.contacts) {

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                    ) {
                        Row (modifier = Modifier.fillMaxWidth()){
                            Column (
                                modifier = Modifier.clickable {
                                    state.id.value = it.id
                                    state.number.value = it.number
                                    state.email.value = it.email
                                    state.name.value = it.name
                                    state.dateOfCreation.value = it.dateOfCreation
                                    navController.navigate(Routes.AddEditScreen.route)
                                }
                            ){
                                Text(text = it.name)
                                Text(text = it.number)
                                Text(text = it.email)
                                Text(text = it.dateOfCreation.toString())
                            }
                            Icon(imageVector = Icons.Rounded.Delete, contentDescription = null,
                                modifier = Modifier.clickable {
                                    state.id.value = it.id
                                    state.number.value = it.number
                                    state.email.value = it.email
                                    state.name.value = it.name
                                    state.dateOfCreation.value = it.dateOfCreation
                                    viewModel.deleteContact()
                                }
                            )
                        }
                    }
                }
            }
        }

    }
}