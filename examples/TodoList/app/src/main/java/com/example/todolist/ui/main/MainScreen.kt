package com.example.todolist.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todolist.R
import com.example.todolist.data.Todo
import com.example.todolist.ui.theme.TodoListTheme

@Composable
fun HomeScreen(
    todoItems: List<Todo>,
    onAddTodo: (text: String) -> Unit = {},
    onToggle: (index: Int) -> Unit = {},
    onDelete: (index: Int) -> Unit = {},
) {
    val focusManager = LocalFocusManager.current

    // 다음을 임포트하면 델리게이티드 프로퍼티(by)를 사용할 수 있음
    // import androidx.compose.runtime.getValue
    // import androidx.compose.runtime.setValue
    // getter 와 setter 를 미리 재정의 해 둔 것
    var text by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("오늘 할 일") }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Row(Modifier.padding(8.dp)) {
                OutlinedTextField(
                    value = text,
                    onValueChange = {
                        text = it
                    },
                    modifier = Modifier.weight(weight = 1f),    // 남은 영역 꽉 채우기
                    shape = RoundedCornerShape(8.dp),   // 둥글게 모양 내기
                    maxLines = 1,   // 한 줄 이상 늘어나지 않도록
                    placeholder = { Text("할 일") },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_add_24),
                            contentDescription = null,
                        )
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),  // 키보드 옵션
                    keyboardActions = KeyboardActions(onDone = {
                        onAddTodo(text)
                        text = ""
                        focusManager.clearFocus()   // 키보드 숨기기
                    }),
                )
            }

            Divider()

            LazyColumn(
                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),   // 상하 좌우 패딩
                verticalArrangement = Arrangement.spacedBy(16.dp),  // 아이템간의 패딩
            ) {
                items(todoItems) { todoItem ->
                    Column {
                        TodoItem(
                            todo = todoItem,
                            onClick = { index ->
                                onToggle(index)
                            },
                            onDeleteClick = { index ->
                                onDelete(index)
                            },
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Divider(color = Color.Black, thickness = 1.dp)
                    }
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val items = listOf(
        Todo("청소", isDone = true),
        Todo("빨래", isDone = false),
        Todo("숙제", isDone = true),
    )

    TodoListTheme {
        HomeScreen(items)
    }
}