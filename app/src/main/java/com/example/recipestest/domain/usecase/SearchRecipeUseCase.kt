package com.example.recipestest.domain.usecase

import com.example.recipestest.domain.model.Recipe
import com.example.recipestest.domain.repository.RecipeRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class SearchRecipesUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    operator fun invoke(query: String): Flow<List<Recipe>> {
        return repository.searchRecipes(query)
    }
}
