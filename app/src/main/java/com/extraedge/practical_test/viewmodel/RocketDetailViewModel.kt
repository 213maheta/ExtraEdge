package com.extraedge.practical_test.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.extraedge.practical_test.model.RocketResponse
import com.extraedge.practical_test.repository.RockerDetailRepository
import com.extraedge.practical_test.room.RocketModel
import com.extraedge.practical_test.sealed.MainEvent
import com.extraedge.practical_test.sealed.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RocketDetailViewModel(val repository:RockerDetailRepository):ViewModel() {

    private var _event = MutableStateFlow<MainEvent>(MainEvent.Idle)
    var event = _event.asStateFlow()

    fun getRockerDetail(id:String) = viewModelScope.launch {
        repository.getRocketDetail(id)
    }

    fun setObserver() = viewModelScope.launch {
        repository.responseType.collect{
            when(it)
            {
                NetworkResult.Loading->{
                    _event.value = MainEvent.ShowProgressBar
                }
                is NetworkResult.Success<*>->{
                    _event.value = MainEvent.ResponseList(it.data as RocketModel)
                }
                is NetworkResult.Error<*>->{
                    _event.value = MainEvent.ShowToast<String>(it.message)
                }
                else -> {}
            }
        }
    }

    suspend fun getRocketDetailLocally(id: String) = viewModelScope.launch {
        withContext(Dispatchers.IO){
            repository.getRocketDetailLocally(id)
        }
    }

    fun showToast(message:String)
    {
        _event.value = MainEvent.ShowToast<String>(message)
    }
}