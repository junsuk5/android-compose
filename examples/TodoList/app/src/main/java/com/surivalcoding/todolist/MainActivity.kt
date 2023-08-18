package com.surivalcoding.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.surivalcoding.todolist.domain.util.TodoAndroidViewModelFactory
import com.surivalcoding.todolist.ui.HomeScreen
import com.surivalcoding.todolist.ui.MainViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val mainViewModel: MainViewModel = viewModel(
                factory = TodoAndroidViewModelFactory(application)
            )

            HomeScreen(viewModel = mainViewModel)
        }
    }
}