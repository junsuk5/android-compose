package com.example.todolist.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Todo(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    val title: String,
    val date: Long = Calendar.getInstance().timeInMillis,
    val isDone: Boolean = false,
)