package com.russia.criminalintent.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.russia.criminalintent.CrimeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CrimeListViewModel : ViewModel() {

    private val crimeRepository =  CrimeRepository.get()

    private val _crimes: MutableStateFlow<List<Crime>> = MutableStateFlow(emptyList())

    val crimes: StateFlow<List<Crime>>
        get() = _crimes.asStateFlow()

    suspend fun addCrime(crime: Crime) {
        crimeRepository.addCrime(crime)
    }

    init {
        viewModelScope.launch {
            crimeRepository.getCrimes().collect {
                _crimes.value = it
            }
        }
    }

}