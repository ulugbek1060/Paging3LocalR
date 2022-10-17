package com.android.paging3test.model.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.android.paging3test.model.User


@Entity(
   tableName = "users",
   indices = [
      Index("name")
   ]
)
data class UserEntity(
   @PrimaryKey(autoGenerate = true) val id: Long,
   @ColumnInfo(collate = ColumnInfo.NOCASE) val name: String,
   val company: String,
   val imageUrl: String,
   val isFavorite: Boolean
) {

   fun toUser(): User = User(
      id = id,
      name = name,
      company = company,
      imageUrl = imageUrl,
      isFavorite = isFavorite
   )
}