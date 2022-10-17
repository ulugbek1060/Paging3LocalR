package com.android.paging3test.model.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.android.paging3test.model.User
import com.android.paging3test.model.database.IdTuple
import com.android.paging3test.model.database.UpdateUserFavoriteTuple
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

   override fun isErrorsEnabled(): Flow<Boolean> = enableErrorsFlow

   override fun setErrorsEnabled(value: Boolean) {
      enableErrorsFlow.value = value
   }

   override fun getPagedUsers(searchBy: String): Flow<PagingData<User>> {
      val loader: UsersPageLoader = { pageIndex, pageSize ->
         getUsers(pageIndex, pageSize, searchBy)
      }
      return Pager(
         config = PagingConfig(
            // for now let's use the same page size for initial
            // and subsequent loads
            pageSize = PAGE_SIZE,
            initialLoadSize = PAGE_SIZE,
            prefetchDistance = PAGE_SIZE / 2,
            enablePlaceholders = false
         ),
         pagingSourceFactory = { UsersPagingSource(loader) }
      ).flow
   }

   override suspend fun setIsFavorite(user: User, isFavorite: Boolean) = withContext(ioDispatcher) {
      delay(1000)
      throwErrorsIfEnabled()

      val tuple = UpdateUserFavoriteTuple(user.id, isFavorite)
      usersDao.setIsFavorite(tuple)
   }

   override suspend fun delete(user: User) = withContext(ioDispatcher) {
      delay(1000)
      throwErrorsIfEnabled()

      usersDao.delete(IdTuple(user.id))
   }

   private suspend fun getUsers(pageIndex: Int, pageSize: Int, searchBy: String): List<User>
       = withContext(ioDispatcher) {

      delay(2000) // some delay to test loading state
      throwErrorsIfEnabled()

      // calculate offset value required by DAO
      val offset = pageIndex * pageSize

      // get page
      val list = usersDao.getUsers(pageSize, offset, searchBy)

      // map UserDbEntity to User
      return@withContext list
         .map(UserEntity::toUser)
   }

   private fun throwErrorsIfEnabled() {
      if (enableErrorsFlow.value) throw IllegalStateException("Error!")
   }

   private companion object {
      const val PAGE_SIZE = 40
   }
}