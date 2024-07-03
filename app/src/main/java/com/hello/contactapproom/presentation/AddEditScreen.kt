package com.hello.contactapproom.presentation

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import java.io.InputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScreen(
    state: ContactState,
    navController: NavController,
    onEvent: () -> Unit
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        if(uri != null) {
            val inputStream: InputStream? = uri.let {
                context.contentResolver.openInputStream(it)
            }
            val bytes = inputStream?.readBytes()
            if(bytes != null){
                state.image.value = bytes
            }
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = "Add Contact")
            })
        }
    ) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            Column {
                Button(onClick = { launcher.launch("image/*") }) {
                    Text(text = "Pick Image")
                }
                OutlinedTextField(value = state.name.value,
                    onValueChange = { state.name.value = it },
                    placeholder = { Text(text = "Enter the name") }
                )

                OutlinedTextField(value = state.number.value,
                    onValueChange = { state.number.value = it },
                    placeholder = { Text(text = "Enter the number") }
                )

                OutlinedTextField(value = state.email.value,
                    onValueChange = { state.email.value = it },
                    placeholder = { Text(text = "Enter the email") }
                )

                Button(onClick = {
                    onEvent()
                    navController.navigateUp()
                }) {
                    Text(text = "Save Contact")
                }
            }
        }
    }
}