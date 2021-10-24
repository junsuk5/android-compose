package com.example.mywebbrowser

import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mywebbrowser.ui.theme.MyWebBrowserTheme


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
    // url을 관찰
    val url = viewModel.url.value

    val focusManager = LocalFocusManager.current

    val (inputUrl, setUrl) = rememberSaveable { mutableStateOf("https://www.google.com") }

    var webView: WebView? = null

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "나만의 웹 브라우저") },
                actions = {
                    IconButton(onClick = {
                        if (webView?.canGoBack() == true) {
                            webView?.goBack()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "back",
                        )
                    }
                    IconButton(onClick = {
                        if (webView?.canGoForward() == true) {
                            webView?.goForward()
                        }
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
                    viewModel.urlChange(inputUrl)
                    focusManager.clearFocus()   // 키보드 숨기기
                })
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 웹뷰 코드를 여기에서 실행함
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    WebView(context).apply {
                        settings.javaScriptEnabled = true
                        webViewClient = WebViewClient()
                        loadUrl(url)
                        webView = this
                    }
                },
                update = { webView ->
                    // url이 변경될 때 마다 호출됨
                    webView.loadUrl(url)
                }
            )
        }
    }

}

@Composable
fun MyWebView(
    modifier: Modifier = Modifier,
    url: String,
) {

}
