package com.hello.contactapproom.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import com.hello.contactapproom.data.database.Contact

data class ContactState(
    val contacts: List<Contact> = emptyList(),
    val id: MutableState<Int> = mutableStateOf(0),
    val name: MutableState<String> = mutableStateOf(""),
    val number: MutableState<String> = mutableStateOf(""),
    val email: MutableState<String> = mutableStateOf(""),
    val dateOfCreation: MutableState<Long> = mutableStateOf(0),
    )