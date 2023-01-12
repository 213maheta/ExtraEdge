package com.extraedge.practical_test.koin

import android.app.Application
import androidx.room.Room
import com.extraedge.practical_test.BuildConfig
import com.extraedge.practical_test.repository.MainRepository
import com.extraedge.practical_test.repository.RockerDetailRepository
import com.extraedge.practical_test.retrofit.ApiInterface
import com.extraedge.practical_test.room.MyDatabase
import com.extraedge.practical_test.room.RocketDao
import com.extraedge.practical_test.viewmodel.MainViewModel
import com.extraedge.practical_test.viewmodel.RocketDetailViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun provideDataBase(application: Application): MyDatabase {
    return Room.databaseBuilder(application, MyDatabase::class.java, "RoomDB")
        .fallbackToDestructiveMigration()
        .build()
}

fun provideDao(dataBase: MyDatabase): RocketDao {
    return dataBase.rocketDao()
}

val rocketDB = module {
    single { provideDataBase(androidApplication()) }
    single { provideDao(get()) }
}

val networkModule = module {
    factory { provideForecastApi(get()) }
    single { provideRetrofit() }
}

fun provideRetrofit(): Retrofit {
    return Retrofit.Builder().baseUrl(BuildConfig.API_URL)
        .addConverterFactory(GsonConverterFactory.create()).build()
}

fun provideForecastApi(retrofit: Retrofit): ApiInterface = retrofit.create(ApiInterface::class.java)

val mymodule = module{
    includes(rocketDB)

    single { MainRepository(get(), get()) }
    single { RockerDetailRepository(get(), get()) }
    viewModel { MainViewModel(get()) }
    viewModel { RocketDetailViewModel(get()) }
}