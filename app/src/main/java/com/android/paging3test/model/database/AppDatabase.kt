package com.android.paging3test.model.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
   version = 1,
   entities = [
      UserEntity::class
   ]
)
abstract class AppDatabase : RoomDatabase() {

   abstract fun userDao(): UserDao
}