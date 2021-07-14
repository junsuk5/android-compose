package com.example.flashlight

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.compose.runtime.produceState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TorchService : Service() {
    private val torch: Torch by lazy {
        Torch(this)
    }

    private var _isRunning = false
    val isRunning = _isRunning

    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getService(): TorchService = this@TorchService
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            // 앱에서 실행할 경우
            "on" -> {
                torch.flashOn()
                _isRunning = true
            }
            "off" -> {
                torch.flashOff()
                _isRunning = false
            }
            // 위젯에서 실행할 경우
            else -> {
                _isRunning = !_isRunning
                if (_isRunning) {
                    torch.flashOn()
                } else {
                    torch.flashOff()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }
}