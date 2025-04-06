package com.example.mywishlistapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
abstract class WishDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun addAWish(wishEntity: Wish)

    @Delete
    abstract suspend fun deleteAWish(wishEntity: Wish)

    @Update
    abstract suspend fun updateAWish(wishEntity: Wish)

    @Query("SELECT * FROM `wish-table`")
    abstract fun getAllWish(): Flow<List<Wish>>

    @Query("SELECT * FROM `wish-table` WHERE id = :id")
    abstract fun getAWish(id: Long): Flow<Wish>

}