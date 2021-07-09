package com.example.mygallery

import android.Manifest
import android.app.Application
import android.content.ContentUris
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import com.example.mygallery.ui.theme.MyGalleryTheme
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.pager.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.absoluteValue

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyGalleryTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    // 권한 여부를 저장
                    val granted = remember { mutableStateOf(false) }

                    // 권한요청을 위한 런처
                    val launcher =
                        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                            granted.value = isGranted
                        }

                    // 권한이 있는지 체크
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        granted.value = true
                    }

                    // 권한이 있다면 사진을 가져오고 화면에 표시
                    if (granted.value) {
                        viewModel.fetchPhotos()
                        HomeScreen(viewModel)
                    } else {
                        // 권한이 없다면 권한을 요청하는 화면을 표시
                        PermissionRequestScreen {
                            // 권한을 요청하는 코드
                            launcher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                        }
                    }
                }
            }
        }
    }
}

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val _photoUris = MutableStateFlow(emptyList<Uri>())
    val photoUris: StateFlow<List<Uri>> = _photoUris

    fun fetchPhotos() {
        val uris = mutableListOf<Uri>()

        // 모든 사진 정보 가져오기
        getApplication<Application>().contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            "${MediaStore.Images.ImageColumns.DATE_TAKEN} DESC" // 찍은 날짜 내림차순
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)

                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                uris.add(contentUri)
            }
        }

        _photoUris.value = uris
    }
}

@Composable
fun PermissionRequestScreen(onClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("권한이 허용되지 않았습니다")
        Button(onClick = onClick) {
            Text("권한 요청")
        }
    }
}

private fun lerp(start: Float, stop: Float, fraction: Float): Float =
    (1 - fraction) * start + fraction * stop

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreen(viewModel: MainViewModel) {
    // 관찰 가능한 데이터
    val photoUris = viewModel.photoUris.collectAsState()
    // Display 10 items
    val pagerState = rememberPagerState(
        pageCount = photoUris.value.size,   // 총 사진의 수
        initialOffscreenLimit = 3,  // 한번에 로딩할 사진의 수
    )

    Column(Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)     // Column 내에서의 비중: 1 = 남은 영역을 모두 차지
                .padding(16.dp)
                .fillMaxSize()
        ) { page ->
            Card(
                Modifier
                    .graphicsLayer {
                        // Calculate the absolute offset for the current page from the
                        // scroll position. We use the absolute value which allows us to mirror
                        // any effects for both directions
                        val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue

                        // We animate the scaleX + scaleY, between 85% and 100%
                        lerp(
                            start = 0.85f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        ).also { scale ->
                            scaleX = scale
                            scaleY = scale
                        }

                        // We animate the alpha, between 50% and 100%
                        alpha = lerp(
                            start = 0.5f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                    }
            ) {
                // Card content
                Image(
                    painter = rememberCoilPainter(
                        request = photoUris.value[page]
                    ),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        // 페이저 인디케이터
        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyGalleryTheme {
        PermissionRequestScreen {

        }
    }
}