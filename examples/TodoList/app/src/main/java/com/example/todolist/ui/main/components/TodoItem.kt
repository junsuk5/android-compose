package com.example.todolist.ui.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todolist.R
import com.example.todolist.domain.model.Todo
import java.text.SimpleDateFormat
import java.util.*

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
fun TodoItemTruePreview() {
    TodoItem(todo = Todo("숙제", Date().time, true))
}

@Preview(showBackground = true)
@Composable
fun TodoItemFalsePreview() {
    TodoItem(todo = Todo("숙제", Date().time, false))
}