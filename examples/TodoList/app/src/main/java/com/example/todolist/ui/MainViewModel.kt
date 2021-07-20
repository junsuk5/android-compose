package com.example.todolist.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.todolist.data.Todo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private var _items = MutableStateFlow<List<Todo>>(emptyList())
    val items: StateFlow<List<Todo>> = _items

    private var _count = AtomicInteger(0)

    fun addTodo(text: String) {
        _items.value = _items.value.toMutableList().apply {
            add(Todo(_count.getAndIncrement(), text, Date().time))
        }
    }

    fun toggle(index: Int) {
        _items.value = _items.value.map {
            if (it.uid == index) {
                it.copy(isDone = !it.isDone)
            } else it
        }
    }

    fun delete(index: Int) {
        _items.value = _items.value.filter { it.uid != index }
    }
}