package com.example.locationbasic

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class LocationViewModel: ViewModel() {
    private val _location = mutableStateOf<LocationData?>(null)

    val location: State<LocationData?> = _location

    fun updateLocation(location: LocationData){
        _location.value = location
    }
}