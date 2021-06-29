package com.example.bmicalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bmicalculator.ui.theme.BmiCalculatorTheme
import kotlin.math.pow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val viewModel: BmiViewModel = viewModel()

            BmiCalculatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") {
                            FirstScreen(
                                navController = navController,
                                bmiViewModel = viewModel
                            )
                        }
                        composable("result") {
                            ResultScreen(
                                navController = navController,
                                bmiViewModel = viewModel
                            )
                        }
                    }
                }
            }
        }
    }
}

class BmiViewModel : ViewModel() {
    // LiveData는 상태 값을 유지하고 UI가 감시하는 값을 저장하는 객체
    private val _text = MutableLiveData("")
    val text: LiveData<String> = _text

    private val _resId = MutableLiveData(R.drawable.ic_baseline_sentiment_very_dissatisfied_24)
    val resId: LiveData<Int> = _resId

    // UI에서 이 함수를 실행하여 값을 변경
    fun onValueChange(newHeight: String, newWeight: String) {
        val bmi = newWeight.toInt() / (newHeight.toInt() / 100.0).pow(2.0)
        when {
            bmi >= 35 -> _text.value = "고도 비만"
            bmi >= 30 -> _text.value = "2단계 비만"
            bmi >= 25 -> _text.value = "1단계 비만"
            bmi >= 23 -> {
                _text.value = "과체중"
                _resId.value = R.drawable.ic_baseline_sentiment_very_dissatisfied_24
            }
            bmi >= 18.5 -> {
                _text.value = "정상"
                _resId.value = R.drawable.ic_baseline_sentiment_satisfied_alt_24
            }
            else -> {
                _text.value = "저체중"
                _resId.value = R.drawable.ic_baseline_sentiment_dissatisfied_24
            }
        }
    }

}

@Composable
fun FirstScreen(navController: NavController, bmiViewModel: BmiViewModel = viewModel()) {
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
                            bmiViewModel.onValueChange(newHeight = height, newWeight = weight)
                            navController.navigate("result") {
                                popUpTo("home")
                            }
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

@Composable
fun ResultScreen(navController: NavController, bmiViewModel: BmiViewModel = viewModel()) {
    val resultText: String by bmiViewModel.text.observeAsState("정상")
    val resId: Int by bmiViewModel.resId.observeAsState(R.drawable.ic_baseline_sentiment_dissatisfied_24)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "비만도 계산기") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigateUp()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "home"
                        )
                    }
                }
            )
        },
        content = {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(resultText, fontSize = 30.sp)
                Spacer(modifier = Modifier.height(50.dp))
                Image(
                    painter = painterResource(id = resId),
                    contentDescription = null,
                    modifier = Modifier.size(100.dp),
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val navController = rememberNavController()

    BmiCalculatorTheme {
        FirstScreen(navController = navController)
    }
}