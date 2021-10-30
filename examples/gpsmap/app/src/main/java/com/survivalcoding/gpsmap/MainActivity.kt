package com.survivalcoding.gpsmap

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var granted by remember { mutableStateOf(false) }

            val launcher =
                rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult = { isGranted ->
                        granted = isGranted
                    }
                )

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                granted = true
            }

            if (granted) {
                val viewModel = viewModel<MainViewModel>()
                lifecycle.addObserver(viewModel)
                MyMap(viewModel = viewModel)
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text("권한이 허용되지 않았습니다")
                    Button(onClick = { launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION) }) {
                        Text("권한 요청")
                    }
                }
            }

        }
    }
}

class MainViewModel(application: Application) : AndroidViewModel(application), LifecycleObserver {
    private val fusedLocationProviderClient: FusedLocationProviderClient =
        FusedLocationProviderClient(application.applicationContext)
    private val locationRequest: LocationRequest
    private val locationCallback: MyLocationCallBack

    private val _state =
        mutableStateOf(MapState(null, PolylineOptions().width(5f).color(Color.RED)))
    val state: State<MapState> = _state

//    private val _location = mutableStateOf<Location?>(null)
//    val location: State<Location?> = _location
//
//    private val _polylineOptions = mutableStateOf(PolylineOptions().width(5f).color(Color.RED))
//    val polylineOptions: State<PolylineOptions> = _polylineOptions

    init {
        locationCallback = MyLocationCallBack()

        locationRequest = LocationRequest.create()
        // GPS 우선
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        // 업데이트 인터벌
        // 위치 정보가 없을 때는 업데이트 안 함
        // 상황에 따라 짧아질 수 있음, 정확하지 않음
        // 다른 앱에서 짧은 인터벌로 위치 정보를 요청하면 짧아질 수 있음
        locationRequest.interval = 10000
        // 정확함. 이것보다 짧은 업데이트는 하지 않음
        locationRequest.fastestInterval = 5000
    }

    @SuppressLint("MissingPermission")
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun addLocationListener() {
        Looper.myLooper()?.let { looper ->
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                looper,
            )
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private fun removeLocationListener() {
        // 현재 위치 요청을 삭제 ②
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    inner class MyLocationCallBack : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)

            val location = locationResult.lastLocation

            val polylineOptions = state.value.polylineOptions

            _state.value = state.value.copy(
                location = location,
                polylineOptions = polylineOptions.add(LatLng(location.latitude, location.longitude))
            )
        }
    }
}

data class MapState(
    val location: Location?,
    val polylineOptions: PolylineOptions,
)

@Composable
fun MyMap(
    viewModel: MainViewModel,
) {
    val map = rememberMapView()
    val state = viewModel.state.value

    AndroidView(
        factory = { map },
        update = { mapView ->
            mapView.getMapAsync { googleMap ->
                state.location?.let {
                    val latLng = LatLng(it.latitude, it.longitude)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))

                    googleMap.addPolyline(state.polylineOptions)
                }
//                val sydney = LatLng(-34.0, 151.0)
//                googleMap.addMarker(
//                    MarkerOptions().position(sydney).title("Marker in Sydney")
//                )
//                googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
            }
        },
    )
}

@Composable
fun rememberMapView(): MapView {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> mapView.onCreate(Bundle())
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> throw IllegalStateException()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    return mapView
}