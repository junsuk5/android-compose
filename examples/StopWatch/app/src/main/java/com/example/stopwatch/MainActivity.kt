package com.example.stopwatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stopwatch.ui.theme.StopWatchTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StopWatchTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    Scaffold(topBar = {
        TopAppBar(title = {
            Text("StopWatch")
        })
    }, bottomBar = {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // 재시작 버튼
            FloatingActionButton(
                onClick = { /*TODO*/ },
                backgroundColor = Color.Red,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_baseline_refresh_24),
                    contentDescription = null,
                )
            }

            // 일시 정지 버튼
            FloatingActionButton(
                onClick = { /*TODO*/ },
                backgroundColor = Color.Green,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_baseline_pause_24),
                    contentDescription = null,
                )
            }

            Button(onClick = { /*TODO*/ }) {
                Text("랩 타임")
            }
        }
    }) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Row(
                verticalAlignment = Alignment.Bottom,
            ) {
                Text("00", fontSize = 100.sp)
                Text("00")
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