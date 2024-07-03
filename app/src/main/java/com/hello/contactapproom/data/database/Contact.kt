package com.hello.contactapproom.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contact_table")
data class Contact(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val number: String,
    var email: String,
    var dateOfCreation: Long,
    var isActive: Boolean,
    val image: ByteArray ?= null,
)