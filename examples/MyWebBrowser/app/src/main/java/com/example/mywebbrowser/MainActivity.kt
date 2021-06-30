package com.example.mywebbrowser

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModel
import com.example.mywebbrowser.ui.theme.MyWebBrowserTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyWebBrowserTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    // url을 관찰
                    val url = viewModel.url.collectAsState()

                    HomeScreen(viewModel) {
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

class MainViewModel : ViewModel() {
    private val _url = MutableStateFlow("https://www.google.com")
    val url: StateFlow<String> = _url

    // Undo, Redo를 위한 스택
    private val _undoStack = Stack<String>()
    private val _redoStack = Stack<String>()

    fun onUrlChange(newUrl: String) {
        _undoStack.push(_url.value)
        _url.value = newUrl
    }

    fun undo() {
        if (_undoStack.isNotEmpty()) {
            _redoStack.push(_url.value)
            _url.value = _undoStack.pop()
        }
    }

    fun redo() {
        if (_redoStack.isNotEmpty()) {
            _undoStack.push(_url.value)
            _url.value = _redoStack.pop()
        }
    }
}

@Composable
fun HomeScreen(viewModel: MainViewModel, webView: @Composable () -> Unit) {
    val focusManager = LocalFocusManager.current

    val (inputUrl, setUrl) = rememberSaveable { mutableStateOf("https://www.google.com") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "나만의 웹 브라우저") },
                actions = {
                    IconButton(onClick = { viewModel.undo() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                    IconButton(onClick = { viewModel.redo() }) {
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
                        viewModel.onUrlChange(inputUrl)
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
    }
}