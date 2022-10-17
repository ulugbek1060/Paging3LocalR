package com.android.paging3test.model.repository

import androidx.paging.PagingData
import com.android.paging3test.model.User
import kotlinx.coroutines.flow.Flow

interface UsersRepository {

   /**
    * Whether errors are enabled or not. The value is listened by the bottom "Enable Errors" checkbox
    * in the MainActivity.
    */
   fun isErrorsEnabled(): Flow<Boolean>

   /**
    * Enable/Disable errors when fetching users.
    */
   fun setErrorsEnabled(value: Boolean)

   /**
    * Get the paging list of users.
    */
   fun getPagedUsers(searchBy: String): Flow<PagingData<User>>

   /**
    * Set the 'star' flag for the specific user.
    * @throws IllegalStateException if the 'Enable errors' checkbox is checked.
    */
   suspend fun setIsFavorite(user: User, isFavorite: Boolean)

   /**
    * Delete the user from the app.
    * @throws IllegalStateException if the 'Enable errors' checkbox is checked.
    */
   suspend fun delete(user: User)
}