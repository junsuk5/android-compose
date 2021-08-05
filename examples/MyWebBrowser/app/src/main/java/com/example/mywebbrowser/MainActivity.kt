package com.example.mywebbrowser

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.mywebbrowser.ui.theme.MyWebBrowserTheme


class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // url을 관찰
            val url = viewModel.url.collectAsState()

            MyWebBrowserTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {

                    HomeScreen(
                        undo = { viewModel.undo() },
                        redo = { viewModel.redo() },
                        onUrlChange = { viewModel.onUrlChange(it) }
                    ) {
                        // 웹사이트를 표시할 웹뷰
                        AndroidView(
                            modifier = Modifier.fillMaxSize(),
                            factory = { context ->
                                WebView(context).apply {
                                    settings.javaScriptEnabled = true
                                    webViewClient = WebViewClient()
                                    loadUrl(url.value)
                                }
                            },
                            update = {
                                // url이 변경될 때 마다 호출됨
                                it.loadUrl(url.value)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(
    undo: () -> Unit = {},
    redo: () -> Unit = {},
    onUrlChange: (url: String) -> Unit = {},
    webView: @Composable () -> Unit
) {
    val focusManager = LocalFocusManager.current

    val (inputUrl, setUrl) = rememberSaveable { mutableStateOf("https://www.google.com") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "나만의 웹 브라우저") },
                actions = {
                    IconButton(onClick = { undo() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                    IconButton(onClick = { redo() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowForward,
                            contentDescription = "forward"
                        )
                    }
                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                // Url 입력 창
                OutlinedTextField(
                    value = inputUrl,
                    onValueChange = setUrl,
                    label = { Text("http://") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),    // 검색아이콘
                    keyboardActions = KeyboardActions(onSearch = {  // 검색 아이콘을 클릭했을 때
                        onUrlChange(inputUrl)
                        focusManager.clearFocus()   // 키보드 숨기기
                    })
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 웹뷰 코드를 여기에서 실행함
                webView()
            }
        }
    )

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyWebBrowserTheme {
        HomeScreen {
            // 웹사이트를 표시할 웹뷰
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(1.dp, Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Text("WebView")
            }
        }
    }
}