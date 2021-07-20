package com.example.todolist.data.memory

import com.example.todolist.data.Todo
import com.example.todolist.data.TodoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

class MemoryTodoRepository : TodoRepository {
    private var _items = MutableStateFlow<List<Todo>>(emptyList())

    private var _count = AtomicInteger(0)

    override fun observeTodos(): Flow<List<Todo>> {
        return _items
    }

    override suspend fun addTodo(text: String) {
        _items.value = _items.value.toMutableList().apply {
            add(Todo(_count.getAndIncrement(), text, Date().time))
        }
    }

    override suspend fun updateTodo(todo: Todo) {
        _items.value = _items.value.map {
            if (it.uid == todo.uid) {
                todo
            } else it
        }
    }

    override suspend fun deleteTodo(todo: Todo) {
        _items.value = _items.value.filter { it.uid != todo.uid }
    }
}