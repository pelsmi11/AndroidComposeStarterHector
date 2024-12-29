package com.example.androidcomposestarterhector.core.ui.components.model

data class ErrorStatus(
    val isError: Boolean = false,
    val errorMsg: String? = null
) {
    companion object {
        fun noError() = ErrorStatus(false, null)
        fun error(message: String) = ErrorStatus(true, message)
    }
}