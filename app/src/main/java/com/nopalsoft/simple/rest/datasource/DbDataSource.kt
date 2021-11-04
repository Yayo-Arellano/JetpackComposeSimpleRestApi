package com.nopalsoft.simple.rest.datasource

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nopalsoft.simple.rest.model.User
import com.nopalsoft.simple.rest.model.UserDao

@Database(entities = [User::class], version = 1)
abstract class DbDataSource : RoomDatabase() {

    abstract fun userDao(): UserDao
}