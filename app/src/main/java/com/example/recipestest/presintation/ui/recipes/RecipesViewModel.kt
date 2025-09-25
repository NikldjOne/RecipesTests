package com.example.recipestest.presintation.ui.recipes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipestest.domain.usecase.GetRecipesUseCase
import com.example.recipestest.domain.usecase.SearchRecipesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    private val getRecipesUseCase: GetRecipesUseCase,
    private val searchRecipesUseCase: SearchRecipesUseCase,
) : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    val recipes = getRecipesUseCase()
        .onStart { _isLoading.postValue(true) }
        .onEach { _isLoading.postValue(false) }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun search(query: String) =
        searchRecipesUseCase(query)
            .onStart { _isLoading.postValue(true) }
            .onEach { _isLoading.postValue(false) }
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun loadRandom() =
        getRecipesUseCase()
            .onStart { _isLoading.postValue(true) }
            .onEach { _isLoading.postValue(false) }

    fun loadByCategory(tag: String?) =
        getRecipesUseCase(tag)
            .onStart { _isLoading.postValue(true) }
            .onEach { _isLoading.postValue(false) }

}
