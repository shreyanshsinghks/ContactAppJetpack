package com.hello.contactapproom.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hello.contactapproom.data.database.Contact
import com.hello.contactapproom.data.database.ContactDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(var database: ContactDatabase) : ViewModel() {
    private var isSortedByName = MutableStateFlow(true)
    private val searchQuery = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    private val contact = combine(isSortedByName, searchQuery) { isSorted, query ->
        Pair(isSorted, query)
    }.flatMapLatest { (isSorted, query) ->
        if (isSorted) {
            database.dao.searchContactsSortedByName("%$query%")
        } else {
            database.dao.searchContactsSortedByDate("%$query%")
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(ContactState())
    val state = combine(_state, contact, isSortedByName, searchQuery) { state, contacts, isSortedByName, query ->
        state.copy(
            contacts = contacts,
            searchQuery = state.searchQuery.apply { value = query }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), ContactState())

    fun saveContact() {
        val contact = Contact(
            id = state.value.id.value,
            name = state.value.name.value,
            number = state.value.number.value,
            email = state.value.email.value,
            dateOfCreation = System.currentTimeMillis(),
            isActive = true,
            image = state.value.image.value
        )
        viewModelScope.launch {
            database.dao.upsertContact(contact)
            resetState()
        }
    }

    fun changeSorting() {
        isSortedByName.value = !isSortedByName.value
    }

    fun deleteContact() {
        val contactDelete = Contact(
            id = state.value.id.value,
            name = state.value.name.value,
            number = state.value.number.value,
            email = state.value.email.value,
            dateOfCreation = state.value.dateOfCreation.value,
            isActive = true,
            image = state.value.image.value
        )
        viewModelScope.launch {
            database.dao.deleteContact(contactDelete)
            resetState()
        }
    }

    fun onSearchQueryChange(query: String) {
        searchQuery.value = query
    }

    private fun resetState() {
        state.value.id.value = 0
        state.value.name.value = ""
        state.value.number.value = ""
        state.value.email.value = ""
        state.value.dateOfCreation.value = 0
        state.value.image.value = null
    }
}