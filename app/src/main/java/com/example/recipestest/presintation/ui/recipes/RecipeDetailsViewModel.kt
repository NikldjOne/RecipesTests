package com.example.recipestest.presintation.ui.recipes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipestest.data.model.RecipeInformationDto
import com.example.recipestest.domain.usecase.GetRecipeDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailsViewModel @Inject constructor(
    private val getRecipeDetailsUseCase: GetRecipeDetailsUseCase
) : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading;

    private val _details = MutableStateFlow<RecipeInformationDto?>(null)
    val details: StateFlow<RecipeInformationDto?> get() = _details

    fun loadDetails(id: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _details.value = getRecipeDetailsUseCase(id)

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
