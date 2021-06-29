package com.example.bmicalculator

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bmicalculator.ui.theme.BmiCalculatorTheme
import kotlin.math.pow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BmiCalculatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    HomeScreen()
                }
            }
        }
    }
}

@Composable
fun HomeScreen() {
    val context = LocalContext.current

    val (height, setHeight) = rememberSaveable { mutableStateOf("") }
    val (weight, setWeight) = rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "비만도 계산기") },
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = height,
                    onValueChange = setHeight,
                    label = { Text("키") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = weight,
                    onValueChange = setWeight,
                    label = { Text("몸무게") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        if (height.isNotEmpty() && weight.isNotEmpty()) {
                            val text: String
                            var resId: Int = R.drawable.ic_baseline_sentiment_dissatisfied_24

                            val bmi = weight.toInt() / (height.toInt() / 100.0).pow(2.0)

                            when {
                                bmi >= 35 -> text = "고도 비만"
                                bmi >= 30 -> text = "2단계 비만"
                                bmi >= 25 -> text = "1단계 비만"
                                bmi >= 23 -> {
                                    text = "과체중"
                                    resId = R.drawable.ic_baseline_sentiment_very_dissatisfied_24
                                }
                                bmi >= 18.5 -> {
                                    text = "정상"
                                    resId = R.drawable.ic_baseline_sentiment_satisfied_alt_24
                                }
                                else -> {
                                    text = "저체중"
                                    resId = R.drawable.ic_baseline_sentiment_dissatisfied_24
                                }
                            }

                            // 다음 화면으로 데이터 전달
                            val intent = Intent(context, ResultActivity::class.java).apply {
                                putExtra("text", text)
                                putExtra("resId", resId)
                            }
                            context.startActivity(intent)
                        }
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("결과")
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BmiCalculatorTheme {
        HomeScreen()
    }
}