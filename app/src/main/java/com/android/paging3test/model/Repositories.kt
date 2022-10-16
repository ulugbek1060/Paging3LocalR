package com.android.paging3test.model

import android.content.Context
import androidx.room.Room
import com.android.paging3test.model.database.AppDatabase
import com.android.paging3test.model.repository.UsersRepository
import com.android.paging3test.model.repository.UsersRepositoryImpel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

object Repositories {
   private lateinit var applicationContext: Context

   private val database: AppDatabase by lazy {
      Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database.db")
         .createFromAsset("initial_database.db")
         .build()
   }

   private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

   val usersRepository: UsersRepository by lazy {
      UsersRepositoryImpel(ioDispatcher, database.userDao())
   }

   fun init(context: Context) {
      applicationContext = context
   }

}