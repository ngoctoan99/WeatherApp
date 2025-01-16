package com.example.weatherapp.utils

class AppFormatter {

    fun formatStringTag(input: String): String {
        return input
            .lowercase()
            .split("_")
            .joinToString(" ").replaceFirstChar { it.uppercase() }
    }
    fun formatStringHide(str: String): String {
        val start = str.take(3)
        val end = str.takeLast(4)
        return "$start...$end"
    }
}