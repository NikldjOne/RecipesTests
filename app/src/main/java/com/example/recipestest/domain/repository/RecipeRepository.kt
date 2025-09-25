package com.example.recipestest.domain.repository

import com.example.recipestest.data.model.RecipeInformationDto
import com.example.recipestest.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipeRepository{
    fun getRecipes(tags: String? = null): Flow<List<Recipe>>
    suspend fun getRecipeInformation(id:Int): RecipeInformationDto
    fun searchRecipes(query: String): Flow<List<Recipe>>
}