package com.example.recipestest.domain.usecase

import com.example.recipestest.domain.model.Recipe
import com.example.recipestest.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecipesUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    operator fun invoke(tags: String? = null): Flow<List<Recipe>> =
        repository.getRecipes(tags)
}