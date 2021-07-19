package com.example.todolist

import java.util.*

data class Todo(
    val uid: Int,
    val title: String,
    val date: Long = Calendar.getInstance().timeInMillis,
    var isDone: Boolean = false,
)