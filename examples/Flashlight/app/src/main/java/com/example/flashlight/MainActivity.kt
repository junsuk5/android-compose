package com.example.flashlight

import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flashlight.ui.theme.FlashlightTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = viewModel<MainViewModel>()

            MainScreen(viewModel)
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