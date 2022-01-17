package com.example.superdmbtimer.presentation.home

/* На данный момент одно действие */
sealed class HomeAction {
    object Navigate : HomeAction()
}