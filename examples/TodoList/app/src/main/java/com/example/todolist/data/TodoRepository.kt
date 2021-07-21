package com.example.todolist.data

import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    fun observeTodos(): Flow<List<Todo>>

    suspend fun addTodo(text: String)

    suspend fun updateTodo(todo: Todo)

    suspend fun deleteTodo(todo: Todo)
}