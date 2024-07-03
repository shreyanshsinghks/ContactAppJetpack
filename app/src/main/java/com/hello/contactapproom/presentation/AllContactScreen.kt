package com.hello.contactapproom.presentation

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.SortByAlpha
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.hello.contactapproom.data.database.Contact
import com.hello.contactapproom.navigation.Routes
import com.hello.contactapproom.ui.theme.backgroundColor
import com.hello.contactapproom.ui.theme.dividerColor
import com.hello.contactapproom.ui.theme.primaryColor
import com.hello.contactapproom.ui.theme.primaryVariant
import com.hello.contactapproom.ui.theme.secondaryColor
import com.hello.contactapproom.ui.theme.surfaceColor
import com.hello.contactapproom.ui.theme.textPrimaryColor
import com.hello.contactapproom.ui.theme.textSecondaryColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllContactScreen(
    viewModel: ContactViewModel,
    state: ContactState,
    navController: NavController
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Contacts", color = surfaceColor) },
                actions = {
                    IconButton(onClick = { viewModel.changeSorting() }) {
                        Icon(Icons.Rounded.SortByAlpha, contentDescription = "Sort", tint = surfaceColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryColor,
                    titleContentColor = surfaceColor
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Routes.AddEditScreen.route) },
                containerColor = secondaryColor
            ) {
                Icon(Icons.Rounded.Add, contentDescription = "Add Contact", tint = surfaceColor)
            }
        },
        containerColor = backgroundColor
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            items(state.contacts) { contact ->
                ContactCard(
                    contact = contact,
                    onEditClick = {
                        // Update state and navigate
                        state.id.value = contact.id
                        state.number.value = contact.number
                        state.email.value = contact.email
                        state.name.value = contact.name
                        state.dateOfCreation.value = contact.dateOfCreation
                        navController.navigate(Routes.AddEditScreen.route)
                    },
                    onDeleteClick = {
                        // Update state and delete
                        state.id.value = contact.id
                        state.number.value = contact.number
                        state.email.value = contact.email
                        state.name.value = contact.name
                        state.dateOfCreation.value = contact.dateOfCreation
                        viewModel.deleteContact()
                    },
                    onCallClick = {
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:${contact.number}")
                        }
                        ContextCompat.startActivity(context, intent, null)
                    }
                )
            }
        }
    }
}

@Composable
fun ContactCard(
    contact: Contact,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onCallClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = surfaceColor),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ContactImage(contact.image, contact.name)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = contact.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = textPrimaryColor
                )
                Text(
                    text = contact.number,
                    style = MaterialTheme.typography.bodyMedium,
                    color = textSecondaryColor
                )
                Text(
                    text = contact.email,
                    style = MaterialTheme.typography.bodySmall,
                    color = textSecondaryColor
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onCallClick) {
                    Icon(Icons.Rounded.Phone, contentDescription = "Call", tint = primaryColor)
                }
                IconButton(onClick = onEditClick) {
                    Icon(Icons.Rounded.Edit, contentDescription = "Edit", tint = secondaryColor)
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(Icons.Rounded.Delete, contentDescription = "Delete", tint = Color.Red.copy(alpha = 0.8f))
                }
            }
        }
    }
}

@Composable
fun ContactImage(image: ByteArray?, name: String) {
    val bitmap = image?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }
    Box(
        modifier = Modifier
            .size(56.dp)
            .background(
                color = if (bitmap != null) Color.Transparent else primaryVariant,
                shape = CircleShape
            )
            .border(1.dp, dividerColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Text(
                text = name.first().toString(),
                color = surfaceColor,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

