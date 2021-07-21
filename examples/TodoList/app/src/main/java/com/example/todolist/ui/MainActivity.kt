package com.example.todolist.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todolist.ui.main.HomeScreen
import com.example.todolist.ui.theme.TodoListTheme
import com.example.todolist.util.TodoAndroidViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val mainViewModel: MainViewModel = viewModel(
                factory = TodoAndroidViewModelFactory(application)
            )
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

