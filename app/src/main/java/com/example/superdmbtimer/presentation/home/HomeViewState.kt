package com.example.superdmbtimer.presentation.home

data class HomeViewState(
    val name: String = "",
    private val start: Int = 0,
    private val end: Int = 1,
    val remain: Int = 0
) {
    private val differ = end - start
    val done = differ - remain
    val donePercent = done.toFloat() / differ.toFloat()
}