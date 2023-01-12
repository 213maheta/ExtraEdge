package com.extraedge.practical_test.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [RocketModel::class], version = 1)
@TypeConverters(Converter::class)
abstract class MyDatabase : RoomDatabase() {
    abstract fun rocketDao(): RocketDao
}
