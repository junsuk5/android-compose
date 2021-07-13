package com.example.flashlight

import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.flashlight.ui.theme.FlashlightTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlashlightTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainScreen(viewModel)
                }
            }
        }
    }
}

class MainViewModel(application: Application) : AndroidViewModel(application) {
    fun flashOn() {
        getApplication<Application>().startService(
            Intent(
                getApplication(),
                TorchService::class.java
            ).apply {
                action = "on"
            })
    }

    fun flashOff() {
        getApplication<Application>().startService(
            Intent(
                getApplication(),
                TorchService::class.java
            ).apply {
                action = "off"
            })
    }
}

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val flash = rememberSaveable { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Text("플래시 On/Off")
        Switch(
            checked = flash.value,
            onCheckedChange = { checked ->
                flash.value = checked

                if (checked) {
                    viewModel.flashOn()
                } else {
                    viewModel.flashOff()
                }
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FlashlightTheme {
//        MainScreen()
    }
}