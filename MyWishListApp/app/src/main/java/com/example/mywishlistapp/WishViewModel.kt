package com.example.mywishlistapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mywishlistapp.data.Wish
import com.example.mywishlistapp.data.WishRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class WishViewModel(
    private val wishRepository: WishRepository = Graph.wishRepository
): ViewModel() {
    var wishTitleState by mutableStateOf("")
    var wishDescriptionState by mutableStateOf("")


    fun onWishTitleChange(title: String){
        wishTitleState = title
    }

    fun onWishDescriptionChange(description: String){
        wishDescriptionState = description
    }

    lateinit var getAllWish: Flow<List<Wish>>

    init {
        viewModelScope.launch {
            getAllWish = wishRepository.getAllWish()
        }
    }

    fun addAWish(wishEntity: Wish){
        viewModelScope.launch(Dispatchers.IO) {
            wishRepository.addAWish(wishEntity)
        }
    }

    fun deleteAWish(wishEntity: Wish){
        viewModelScope.launch(Dispatchers.IO) {
            wishRepository.deleteAWish(wishEntity)
        }
    }

    fun getAWish(id: Long): Flow<Wish>{
        return wishRepository.getAWish(id)
    }

    fun updateWish(wishEntity: Wish){
        viewModelScope.launch(Dispatchers.IO) {
            wishRepository.updateAWish(wishEntity)
        }
    }
}