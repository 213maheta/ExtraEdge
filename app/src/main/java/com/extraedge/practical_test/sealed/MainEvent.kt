package com.extraedge.practical_test.sealed

sealed class MainEvent{
    object ShowProgressBar:MainEvent()
    object Idle:MainEvent()
    data class ResponseList<T>(val data: T):MainEvent()
    data class ShowToast<T>(val message:String):MainEvent()
}
