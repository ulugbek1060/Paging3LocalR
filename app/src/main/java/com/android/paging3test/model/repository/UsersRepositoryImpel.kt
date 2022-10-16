package com.android.paging3test.model.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.android.paging3test.model.*
import com.android.paging3test.model.database.UserDao
import com.android.paging3test.model.database.UserEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

class UsersRepositoryImpel(
   private val ioDispatcher: CoroutineDispatcher,
   private val usersDao: UserDao
) : UsersRepository {

   private val enableErrorsFlow = MutableStateFlow(false)

   override fun isErrorsEnable(): Flow<Boolean> = enableErrorsFlow

   override fun setErrorsEnabled(value: Boolean) {
      enableErrorsFlow.value = value
   }

   override fun getPagedUsers(searchBy: String): Flow<PagingData<User>> {
      val loader: UsersPageLoader = { pageIndex, pageSize ->
         getUsers(pageIndex, pageSize, searchBy)
      }
      return Pager(
         config = PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = false
         ),
         pagingSourceFactory = { UsersPagingSource(loader, PAGE_SIZE) }
      ).flow
   }

   private suspend fun getUsers(pageIndex: Int, pageSize: Int, searchBy: String): List<User>
       = withContext(ioDispatcher) {

      delay(2000) // some delay to test loading state

      // if "Enable Errors" checkbox is checked -> throw exception
      if (enableErrorsFlow.value) throw IllegalStateException("Error!")

      // calculate offset value required by DAO
      val offset = pageIndex * pageSize

      // get page
      val list = usersDao.getUsers(pageSize, offset, searchBy)

      // map UserDbEntity to User
      return@withContext list.map(UserEntity::toUser)
   }

   private companion object {
      const val PAGE_SIZE = 20
   }
}