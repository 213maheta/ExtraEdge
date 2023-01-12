package com.extraedge.practical_test.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.extraedge.practical_test.model.Height

@Entity
data class RocketModel(

    @PrimaryKey
    val id:String,
    val name:String,
    val country:String,
    val engine_count:Int,
    val flicker_image: ImageData?,
    val activeStatus: Boolean,
    val costPerLaunch: Int,
    val description: String,
    val wikipediaLink: String,
    val height: String,
    val diameter: String,
    val successRatio:Int
)
