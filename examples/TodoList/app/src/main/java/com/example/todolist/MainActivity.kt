package com.example.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todolist.ui.theme.TodoListTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoListTheme {
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
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("오늘 할 일") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO*/ }) {
                Text("+")
            }
        }
    ) {
        Body()
    }


}

@Composable
fun Body() {
    // 다음을 임포트하면 델리게이티드 프로퍼티(by)를 사용할 수 있음
    // import androidx.compose.runtime.getValue
    // import androidx.compose.runtime.setValue
    // getter 와 setter 를 미리 재정의 해 둔 것
    var text by rememberSaveable { mutableStateOf("") }

    Column(Modifier.fillMaxSize()) {
        Row(Modifier.padding(8.dp)) {
            TextField(
                value = text,
                onValueChange = {
                    text = it
                },
                modifier = Modifier.weight(weight = 1f)
            )

            Button(onClick = {

            }) {
                Text("추가")
            }
        }

        Divider()

        LazyColumn(
            contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            items(50) { index ->
                Column {
                    Text("2021/07/14")
                    Text("$index 번째")
                    Divider()
                }
            }
        }

    }
}


@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TodoListTheme {
        MainScreen()
    }
}