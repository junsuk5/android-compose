package com.example.mywebbrowser

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import java.util.*

class MainViewModel : ViewModel() {
    private val _url = mutableStateOf("https://www.google.com")
    val url: State<String> = _url

    // Undo, Redo를 위한 스택
    private val _undoStack = Stack<String>()
    private val _redoStack = Stack<String>()

    val undoable = _undoStack.isNotEmpty()
    val redoable = _redoStack.isNotEmpty()

    fun urlChange(newUrl: String) {
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