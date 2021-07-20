package com.example.todolist.ui

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todolist.data.TodoRepository
import com.example.todolist.data.memory.MemoryTodoRepository
import com.example.todolist.ui.main.HomeScreen
import com.example.todolist.ui.theme.TodoListTheme

class MainActivity : ComponentActivity() {
    //    private val viewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val todoRepository = MemoryTodoRepository()
            val mainViewModel: MainViewModel =
                viewModel(factory = TodoAndroidViewModelFactory(application, todoRepository))
            val todoItems by mainViewModel.items.collectAsState()

            TodoListTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    HomeScreen(
                        todoItems = todoItems,
                        onAddTodo = mainViewModel::addTodo,
                        onToggle = mainViewModel::toggle,
                        onDelete = mainViewModel::delete,
                    )
                }
            }
        }
    }
}

class TodoAndroidViewModelFactory(
    private val application: Application,
    private val repository: TodoRepository
) : ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(application, repository) as T
        }
        return super.create(modelClass)
    }

}