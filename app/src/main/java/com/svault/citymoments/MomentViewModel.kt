package com.svault.citymoments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MomentViewModel @Inject constructor(
    private val repository: MomentRepository
) : ViewModel() {
    val moments: StateFlow<List<ModelMoment>> = repository.getAllMoments()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addMoment(moment: ModelMoment) {
        viewModelScope.launch {
            repository.addMoment(moment)
        }
    }

    fun deleteMoment(moment: ModelMoment) {
        viewModelScope.launch {
            repository.deleteMoment(moment)
        }
    }
}