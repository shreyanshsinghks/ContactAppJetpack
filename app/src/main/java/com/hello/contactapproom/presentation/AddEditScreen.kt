package com.hello.contactapproom.presentation

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddAPhoto
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hello.contactapproom.ui.theme.backgroundColor
import com.hello.contactapproom.ui.theme.primaryColor
import com.hello.contactapproom.ui.theme.primaryVariant
import com.hello.contactapproom.ui.theme.surfaceColor
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
            TopAppBar(
                title = { Text("Add Contact", color = surfaceColor) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.Rounded.ArrowBackIosNew, contentDescription = "Back", tint = surfaceColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryColor,
                    titleContentColor = surfaceColor
                )
            )
        },
        containerColor = backgroundColor
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                ContactImageEdit(state.image.value, state.name.value)
            }

            OutlinedButton(
                onClick = { launcher.launch("image/*") },
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = primaryColor
                ),
                border = BorderStroke(1.dp, primaryColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    Icons.Rounded.AddAPhoto,
                    contentDescription = "Add Photo",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Choose Photo")
            }

            OutlinedTextField(
                value = state.name.value,
                onValueChange = { state.name.value = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryColor,
                    focusedLabelColor = primaryColor,
                    cursorColor = primaryColor
                ),
                singleLine = true
            )

            OutlinedTextField(
                value = state.number.value,
                onValueChange = { state.number.value = it },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryColor,
                    focusedLabelColor = primaryColor,
                    cursorColor = primaryColor
                ),
                singleLine = true
            )

            OutlinedTextField(
                value = state.email.value,
                onValueChange = { state.email.value = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryColor,
                    focusedLabelColor = primaryColor,
                    cursorColor = primaryColor
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    onEvent()
                    navController.navigateUp()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Save Contact", style = MaterialTheme.typography.titleMedium)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ContactImageEdit(image: ByteArray?, name: String) {
    val bitmap = image?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }
    Box(
        modifier = Modifier
            .size(120.dp)
            .background(
                color = if (bitmap != null) Color.Transparent else primaryVariant,
                shape = CircleShape
            )
            .border(2.dp, primaryColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Text(
                text = name.firstOrNull()?.toString() ?: "?",
                color = surfaceColor,
                style = MaterialTheme.typography.headlineLarge
            )
        }
    }
}