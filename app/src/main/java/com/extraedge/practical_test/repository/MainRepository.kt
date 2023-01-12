package com.extraedge.practical_test.repository

import com.extraedge.practical_test.model.RocketResponse
import com.extraedge.practical_test.retrofit.ApiInterface
import com.extraedge.practical_test.room.ImageData
import com.extraedge.practical_test.room.RocketDao
import com.extraedge.practical_test.room.RocketModel
import com.extraedge.practical_test.sealed.NetworkResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainRepository(val mainApi: ApiInterface, val rocketDao: RocketDao) {

    private var _responseType = MutableStateFlow<NetworkResult>(NetworkResult.Empty)
    var responseType = _responseType.asStateFlow()

    suspend fun getRocketList()
    {
        _responseType.value = NetworkResult.Loading
        try {
            val response = mainApi.getRocketList()
            if (response.code() == 200) {
                val rocketList = response.body()
                val rocketTrimmedList = changeModel(rocketList!!)
                _responseType.value = NetworkResult.Success(rocketTrimmedList)
                CoroutineScope(Dispatchers.IO).launch {
                    rocketDao.insertAll(rocketTrimmedList)
                }
            } else {
                _responseType.value = NetworkResult.Error<String>(response.message())
            }
        } catch (e: Exception) {
            _responseType.value = NetworkResult.Error<String>(e.message?:"")
        }
    }

    suspend fun getLocalRocketList()
    {
        _responseType.value = NetworkResult.Loading
        _responseType.value = NetworkResult.Success(rocketDao.getAll())
    }

    suspend fun changeModel(list:List<RocketResponse>): MutableList<RocketModel> {
        val newList = mutableListOf<RocketModel>()
        list.forEach {
            newList.add(
                RocketModel(
                    id = it.id?:"",
                    name = it.name?:"",
                    country = it.country?:"",
                    engine_count = it.engines?.number?:0,
                    flicker_image = ImageData(imagelist = it.flickrImages),
                    activeStatus = it.active?:false,
                    costPerLaunch = it.costPerLaunch?:0,
                    description = it.description?:"",
                    wikipediaLink = it.wikipedia?:"",
                    height = it.height.toString(),
                    diameter = it.diameter.toString(),
                    successRatio = it.successRatePct?:0
                )
            )
        }
        return newList
    }

}