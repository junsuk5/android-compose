package com.surivalcoding.todolist.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.surivalcoding.todolist.R
import com.surivalcoding.todolist.ui.main.components.TodoItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val focusManager = LocalFocusManager.current

    // 다음을 임포트하면 델리게이티드 프로퍼티(by)를 사용할 수 있음
    // import androidx.compose.runtime.getValue
    // import androidx.compose.runtime.setValue
    // getter 와 setter 를 미리 재정의 해 둔 것
    var text by rememberSaveable { mutableStateOf("") }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
                            painter = painterResource(id = R.drawable.baseline_add_24),
                            contentDescription = null,
                        )
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),  // 키보드 옵션
                    keyboardActions = KeyboardActions(onDone = {
                        viewModel.addTodo(text)
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
                items(viewModel.items.value) { todoItem ->
                    Column {
                        TodoItem(
                            todo = todoItem,
                            onClick = { index ->
                                viewModel.toggle(index)
                            },
                            onDeleteClick = { index ->
                                viewModel.delete(index)
                                scope.launch {
                                    val result = snackbarHostState.showSnackbar(
                                        message = "할 일 삭제됨",
                                        actionLabel = "취소",
                                    )

                                    if (result == SnackbarResult.ActionPerformed) {
                                        viewModel.restoreTodo()
                                    }
                                }
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