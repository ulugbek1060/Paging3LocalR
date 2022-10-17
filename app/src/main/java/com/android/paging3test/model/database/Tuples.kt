package com.android.paging3test.model.database

/**
 * Tuple for updating favorite ("star") flag for the user with the specified ID.
 */
data class UpdateUserFavoriteTuple(
   val id: Long,
   val isFavorite: Boolean
)

/**
 * Tuple for deleting user by ID.
 */
data class IdTuple(
   val id: Long
)