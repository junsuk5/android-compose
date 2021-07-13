package com.example.xylophone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.xylophone.ui.theme.XylophoneTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            XylophoneTheme {
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
    val keys = listOf(
        Pair("도", Color.Red),
        Pair("레", Color(0xFFFF9800)),
        Pair("미", Color(0xFFFFC107)),
        Pair("파", Color(0xFF8BC34A)),
        Pair("솔", Color(0xFF2196F3)),
        Pair("라", Color(0xFF3F51B5)),
        Pair("시", Color(0xFF673AB7)),
        Pair("도", Color.Red),
    )

    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        keys.forEachIndexed { index, key ->
            val padding = (index + 2) * 8   // 16, 24, 32, ...
            Keyboard(
                modifier = Modifier.padding(top = padding.dp, bottom = padding.dp),
                text = key.first,
                color = key.second
            )
        }
    }
}

@Composable
fun Keyboard(modifier: Modifier = Modifier, text: String, color: Color) {
    Box(
        modifier = modifier
            .width(50.dp)
            .fillMaxHeight()
            .background(color = color)
    ) {
        Text(
            text,
            style = TextStyle(color = Color.White, fontSize = 20.sp),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    XylophoneTheme {
        MainScreen()
    }
}