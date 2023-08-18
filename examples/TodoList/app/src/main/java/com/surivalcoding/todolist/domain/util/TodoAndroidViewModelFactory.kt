package com.surivalcoding.todolist.domain.util

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.surivalcoding.todolist.data.repository.RoomTodoRepository
import com.surivalcoding.todolist.domain.repository.TodoRepository
import com.surivalcoding.todolist.ui.MainViewModel

class TodoAndroidViewModelFactory(
    private val application: Application,
    private val repository: TodoRepository = RoomTodoRepository(application)
) : ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(application, repository) as T
        }
        return super.create(modelClass)
    }
}