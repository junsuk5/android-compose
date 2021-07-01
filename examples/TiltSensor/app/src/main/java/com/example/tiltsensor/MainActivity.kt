package com.example.tiltsensor

import android.app.Application
import android.content.Context
import android.content.pm.ActivityInfo
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.*
import com.example.tiltsensor.ui.theme.TiltSensorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // 화면이 꺼지지 않게 하기
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        // 화면이 가로 모드로 고정되게 하기
        requestedOrientation = Activitynfo.SCREEN_ORIENTATION_LANDSCAPE
        super.onCreate(savedInstanceState)
        setContent {
            TiltSensorTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    AndroidView(modifier = Modifier.fillMaxSize(),
                        factory = { context ->
                            TiltView(context)
                        },
                        update = { view ->
                        }
                    )
                }
            }
        }
    }
}

class MainViewModel(application: Application) : AndroidViewModel(application),
    SensorEventListener,
    LifecycleObserver {
    private val sensorManager by lazy {
        application.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun registerSensor() {
        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun unregisterSensor() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            Log.d(
                "MainViewModel",
                "onSensorChanged x: ${event.values[0]}, y: ${event.values[1]}, z: ${event.values[2]}"
            )
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
}

@Composable
fun HomeScreen(viewModel: MainViewModel) {

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TiltSensorTheme {
        Surface(color = MaterialTheme.colors.background) {
            Greeting("Android")
        }
    }
}