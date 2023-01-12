package com.extraedge.practical_test.repository

import android.net.ConnectivityManager
import androidx.core.content.ContextCompat.getSystemService
import com.extraedge.practical_test.model.RocketResponse
import com.extraedge.practical_test.retrofit.ApiInterface
import com.extraedge.practical_test.room.ImageData
import com.extraedge.practical_test.room.RocketDao
import com.extraedge.practical_test.room.RocketModel
import com.extraedge.practical_test.sealed.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class RockerDetailRepository(val mainApi: ApiInterface, val rocketDao: RocketDao) {

    private var _responseType = MutableStateFlow<NetworkResult>(NetworkResult.Empty)
    var responseType = _responseType.asStateFlow()

    suspend fun getRocketDetail(id:String)
    {
        try {
            val response = mainApi.getRecketDetail(id)
            if (response.code() == 200) {
                val rocketDetail = response.body()
                _responseType.value = NetworkResult.Success(rocketDetail?.let { changeModel(it) })
            } else {
                _responseType.value = NetworkResult.Error<String>(response.message())
            }
        } catch (e: Exception) {
            _responseType.value = NetworkResult.Error<String>(e.message?:"")
        }
    }

    suspend fun getRocketDetailLocally(id: String)
    {
        _responseType.value =  NetworkResult.Success(rocketDao.getSingleRocket(id))
    }

    suspend fun changeModel(rockerResponse: RocketResponse): RocketModel {
        return  RocketModel(
            id = rockerResponse.id?:"",
            name = rockerResponse.name?:"",
            country = rockerResponse.country?:"",
            engine_count = rockerResponse.engines?.number?:0,
            flicker_image = ImageData(imagelist = rockerResponse.flickrImages),
            activeStatus = rockerResponse.active?:false,
            costPerLaunch = rockerResponse.costPerLaunch?:0,
            description = rockerResponse.description?:"",
            wikipediaLink = rockerResponse.wikipedia?:"",
            height = rockerResponse.height.toString(),
            diameter = rockerResponse.diameter.toString(),
            successRatio = rockerResponse.successRatePct?:0
        )
    }

}