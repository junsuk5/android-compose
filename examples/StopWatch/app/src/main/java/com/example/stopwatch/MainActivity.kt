package com.example.stopwatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.example.stopwatch.ui.theme.StopWatchTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*
import kotlin.concurrent.timer

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val sec = viewModel.sec.collectAsState()
            val milli = viewModel.milli.collectAsState()
            val isRunning = viewModel.isRunning.collectAsState()
            val lapTimes = viewModel.lapTimes.collectAsState()

            StopWatchTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainScreen(
                        sec = sec.value,
                        milli = milli.value,
                        isRunning = isRunning.value,
                        lapTimes = lapTimes.value,
                        onReset = { viewModel.reset() },
                        onToggle = { viewModel.toggle() },
                        onLapTime = { viewModel.recordLapTime() },
                    )
                }
            }
        }
    }
}

class MainViewModel : ViewModel() {
    private var time = 0
    private var timerTask: Timer? = null

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning

    private val _sec = MutableStateFlow(0)
    val sec: StateFlow<Int> = _sec

    private val _milli = MutableStateFlow(0)
    val milli: StateFlow<Int> = _milli

    private var lap = 1

    private val _lapTimes = MutableStateFlow(mutableListOf<String>())
    val lapTimes: StateFlow<List<String>> = _lapTimes

    fun toggle() {
        if (_isRunning.value) {
            pause()
        } else {
            start()
        }
    }

    private fun start() {
        _isRunning.value = true

        timerTask = timer(period = 10) {
            time++
            print(time)
            _sec.value = time / 100
            _milli.value = time % 100
        }
    }

    private fun pause() {
        _isRunning.value = false
        timerTask?.cancel()
    }

    fun recordLapTime() {
        _lapTimes.value.add(0, "$lap LAP : ${sec.value}.${milli.value}")
        lap++
    }

    fun reset() {
        timerTask?.cancel()

        // 모든 변수 초기화
        time = 0
        _isRunning.value = false
        _sec.value = 0
        _milli.value = 0

        // 모든 랩타임 제거
        _lapTimes.value.clear()
        lap = 1
    }
}

@Composable
fun MainScreen(
    sec: Int = 0, milli: Int = 0, isRunning: Boolean = false,
    lapTimes: List<String> = listOf(
        "3 LAP: 2.55",
        "2 LAP: 1.55",
        "1 LAP: 0.55",
    ),
    onReset: () -> Unit = {},
    onToggle: () -> Unit = {},
    onLapTime: () -> Unit = {},
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("StopWatch") }
            )
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // 여백
            Spacer(modifier = Modifier.height(40.dp))

            // 초, 밀리초
            Row(
                verticalAlignment = Alignment.Bottom,
            ) {
                Text("$sec", fontSize = 100.sp)
                Text("$milli")
            }

            // 여백
            Spacer(modifier = Modifier.height(16.dp))

            // 랩 타임
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
            ) {
                lapTimes.forEach { lapTime ->
                    Text(lapTime)
                }
            }

            // 하단 레이아웃
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // 재시작 버튼
                FloatingActionButton(
                    onClick = { onReset() },
                    backgroundColor = Color.Red,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_baseline_refresh_24),
                        contentDescription = null,
                    )
                }

                // 일시 정지 버튼
                FloatingActionButton(
                    onClick = { onToggle() },
                    backgroundColor = Color.Green,
                ) {
                    Image(
                        painter = painterResource(
                            id =
                            if (isRunning) R.drawable.ic_baseline_pause_24
                            else R.drawable.ic_baseline_play_arrow_24
                        ),
                        contentDescription = null,
                    )
                }

                Button(onClick = { onLapTime() }) {
                    Text("랩 타임")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    StopWatchTheme {
        MainScreen()
    }
}