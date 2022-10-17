package com.android.paging3test.model.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {

   @Query(
      "SELECT * FROM users " +
          "WHERE :searchBy = '' OR name LIKE '%' || :searchBy || '%' " + // search substring
          "ORDER BY name " +  // sort by user name
          "LIMIT :limit OFFSET :offset"
   ) // return max :limit number of users starting from :offset position
   suspend fun getUsers(limit: Int, offset: Int, searchBy: String = ""): List<UserEntity>

   @Update(entity = UserEntity::class)
   suspend fun setIsFavorite(tuple: UpdateUserFavoriteTuple)

   @Delete(entity = UserEntity::class)
   suspend fun delete(id: IdTuple)

}