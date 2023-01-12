package com.extraedge.practical_test.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.extraedge.practical_test.model.RocketResponse
import com.extraedge.practical_test.repository.MainRepository
import com.extraedge.practical_test.room.RocketDao
import com.extraedge.practical_test.room.RocketModel
import com.extraedge.practical_test.sealed.MainEvent
import com.extraedge.practical_test.sealed.NetworkResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(val repository: MainRepository) : ViewModel() {

    private var _event = MutableStateFlow<MainEvent>(MainEvent.Idle)
    var event = _event.asStateFlow()

    fun getRocketList() = viewModelScope.launch {
        repository.getRocketList()
    }

    fun setObserver() = viewModelScope.launch {
        repository.responseType.collect{
            when(it)
            {
                NetworkResult.Loading->{
                    _event.value = MainEvent.ShowProgressBar
                }
                is NetworkResult.Success<*>->{
                    _event.value = MainEvent.ResponseList(it.data as List<RocketModel>)
                }
                is NetworkResult.Error<*>->{
                    _event.value = MainEvent.ShowToast<String>(it.message)
                }
                else -> {}
            }
        }
    }

    fun getLocallyRocketList() = viewModelScope.launch {
        withContext(Dispatchers.IO){
            repository.getLocalRocketList()
        }
    }

    fun showToast(message:String)
    {
        _event.value = MainEvent.ShowToast<String>(message)
    }

}