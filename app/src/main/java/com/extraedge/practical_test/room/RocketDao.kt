package com.extraedge.practical_test.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RocketDao {

    @Query("SELECT * FROM RocketModel")
    fun getAll(): List<RocketModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(rocketModels: List<RocketModel>)

    @Query("SELECT * FROM RocketModel WHERE id=:id ")
    fun getSingleRocket(id: String): RocketModel

}