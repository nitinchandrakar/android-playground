package com.example.mywishlistapp.data

import kotlinx.coroutines.flow.Flow

class WishRepository(private val wishDao: WishDao) {
    suspend fun addAWish(wishEntity: Wish) {
        wishDao.addAWish(wishEntity)
    }

    fun getAllWish(): Flow<List<Wish>> = wishDao.getAllWish()

    fun getAWish(id: Long): Flow<Wish> {
        return wishDao.getAWish(id)
    }

    suspend fun deleteAWish(wishEntity: Wish) {
        wishDao.deleteAWish(wishEntity)
    }

    suspend fun updateAWish(wishEntity: Wish) {
        wishDao.updateAWish(wishEntity)
    }
}