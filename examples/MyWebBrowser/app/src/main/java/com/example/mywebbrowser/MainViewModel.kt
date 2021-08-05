package com.example.mywebbrowser

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*

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