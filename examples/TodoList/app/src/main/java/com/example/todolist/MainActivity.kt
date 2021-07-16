package com.example.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
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
            OutlinedTextField(
                value = text,
                onValueChange = {
                    text = it
                },
                modifier = Modifier.weight(weight = 1f),    // 남은 영역 꽉 채우기
                shape = RoundedCornerShape(8.dp),   // 둥글게 모양 내기
                maxLines = 1,   // 한 줄 이상 늘어나지 않도록
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),  // 키보드 옵션
                placeholder = { Text("할 일") },
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_add_24),
                        contentDescription = null,
                    )
                }
            )
        }

        Divider()

        LazyColumn(
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),   // 상하 좌우 패딩
            verticalArrangement = Arrangement.spacedBy(16.dp),  // 아이템간의 패딩
        ) {
            items(50) { index ->
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        // 삭제 아이콘
                        Icon(
                            modifier = Modifier.padding(end = 8.dp),
                            painter = painterResource(id = R.drawable.ic_baseline_delete_24),
                            contentDescription = null,
                            tint = Color(0xFFA51212),
                        )

                        // 내용
                        Column(
                            modifier = Modifier.weight(1f),
                        ) {
                            Text("2021/07/14")
                            Text("$index 번째")
                        }

                        // 완료 아이콘
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_done_24),
                            contentDescription = null,
                            tint = Color(0xFF00BCD4),
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Divider(color = Color.Black, thickness = 1.dp)
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
fun item1() {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // 삭제 아이콘
            Icon(
                modifier = Modifier.padding(end = 8.dp),
                painter = painterResource(id = R.drawable.ic_baseline_delete_24),
                contentDescription = null,
                tint = Color(0xFFA51212),
            )

            // 내용
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text("2021/07/14")
                Text("1 번째")
            }

            // 완료 아이콘
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_done_24),
                contentDescription = null,
                tint = Color(0xFF00BCD4),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TodoListTheme {
        MainScreen()
    }
}