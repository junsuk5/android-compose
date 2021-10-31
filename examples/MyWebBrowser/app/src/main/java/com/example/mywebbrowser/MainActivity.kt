package com.example.mywebbrowser

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = viewModel<MainViewModel>()

            HomeScreen(viewModel = viewModel)
        }
    }
}

@Composable
fun HomeScreen(viewModel: MainViewModel) {
    val focusManager = LocalFocusManager.current

    val (inputUrl, setUrl) = rememberSaveable { mutableStateOf("https://www.google.com") }

    var url by remember { mutableStateOf("https://www.google.com") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "나만의 웹 브라우저") },
                actions = {
                    IconButton(onClick = {
                        viewModel.undo()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "back",
                        )
                    }
                    IconButton(onClick = {
                        viewModel.redo()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowForward,
                            contentDescription = "forward",
                        )
                    }
                }
            )
        }
    ) {
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
                    viewModel.url.value = inputUrl
                    url = inputUrl
                    focusManager.clearFocus()   // 키보드 숨기기
                })
            )

            Spacer(modifier = Modifier.height(16.dp))

            MyWebView(viewModel = viewModel)
        }
    }

}

@Composable
fun MyWebView(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
) {
    val scope = rememberCoroutineScope()
    val webView = rememberWebView()

    // 웹뷰 코드를 여기에서 실행함
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { webView },
        update = { webView ->
            // url이 변경될 때 마다 호출됨
            webView.loadUrl(viewModel.url.value)

            scope.launch {
                viewModel.undoSharedFlow.collectLatest { goBack ->
                    if (goBack && webView.canGoBack()) {
                        webView.goBack()
                    }
                }
            }

            scope.launch {
                viewModel.redoSharedFlow.collectLatest { goForward ->
                    if (goForward && webView.canGoForward()) {
                        webView.goForward()
                    }
                }
            }
        }
    )
}

@Composable
fun rememberWebView(): WebView {
    val context = LocalContext.current
    val webView = remember {
        WebView(context).apply {
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
        }
    }

    return webView
}