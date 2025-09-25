package com.example.recipestest.domain.usecase

import com.example.recipestest.domain.repository.RecipeRepository
import javax.inject.Inject

class GetRecipeDetailsUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    suspend operator fun invoke(id: Int) = repository.getRecipeInformation(id)
}
