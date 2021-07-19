package com.example.todolist

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todolist.ui.theme.TodoListTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

class MainActivity : ComponentActivity() {
//    private val viewModel by viewModels<MainViewModel>()

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

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private var _items = MutableStateFlow<List<Todo>>(emptyList())
    val items: StateFlow<List<Todo>> = _items

    private var _count = AtomicInteger(0)

    fun addTodo(text: String) {
        _items.value = _items.value.toMutableList().apply {
            add(Todo(_count.getAndIncrement(), text, Date().time))
        }
    }

    fun toggle(index: Int) {
        _items.value = _items.value.map {
            if (it.uid == index) {
                it.copy(isDone = !it.isDone)
            } else it
        }
    }

    fun delete(index: Int) {
        _items.value = _items.value.filter { it.uid != index }
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
fun Body(viewModel: MainViewModel = viewModel()) {
    val focusManager = LocalFocusManager.current

    // 다음을 임포트하면 델리게이티드 프로퍼티(by)를 사용할 수 있음
    // import androidx.compose.runtime.getValue
    // import androidx.compose.runtime.setValue
    // getter 와 setter 를 미리 재정의 해 둔 것
    var text by rememberSaveable { mutableStateOf("") }
    val todoItems by viewModel.items.collectAsState()

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
                placeholder = { Text("할 일") },
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_add_24),
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
            items(todoItems) { todoItem ->
                Column {
                    TodoItem(
                        todo = todoItem,
                        onClick = { index ->
                            viewModel.toggle(index)
                        },
                        onDeleteClick = { index ->
                            viewModel.delete(index)
                        },
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Divider(color = Color.Black, thickness = 1.dp)
                }
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun TodoItemTruePreview() {
    TodoItem(todo = Todo(0, "숙제", Date().time, true))
}

@Preview(showBackground = true)
@Composable
fun TodoItemFalsePreview() {
    TodoItem(todo = Todo(0, "숙제", Date().time, false))
}

@Composable
fun TodoItem(
    todo: Todo,
    onClick: (todo: Int) -> Unit = {},
    onDeleteClick: (id: Int) -> Unit = {},
) {
    val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    Column(
        Modifier
            .padding(8.dp)
            .clickable { onClick(todo.uid) }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // 삭제 아이콘
            Icon(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clickable { onDeleteClick(todo.uid) },
                painter = painterResource(id = R.drawable.ic_baseline_delete_24),
                contentDescription = null,
                tint = Color(0xFFA51212),
            )

            // 내용
            Column(
                modifier = Modifier.weight(1f),
            ) {

                Text(
                    format.format(Date(todo.date)),
                    color = if (todo.isDone) Color.Gray else Color.Black,
                    style = TextStyle(textDecoration = if (todo.isDone) TextDecoration.LineThrough else TextDecoration.None),
                )
                Text(
                    todo.title,
                    color = if (todo.isDone) Color.Gray else Color.Black,
                    style = TextStyle(textDecoration = if (todo.isDone) TextDecoration.LineThrough else TextDecoration.None),
                )
            }

            if (todo.isDone) {
                // 완료 아이콘
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_done_24),
                    contentDescription = null,
                    tint = Color(0xFF00BCD4),
                )
            }
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