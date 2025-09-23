package com.example.recipestest.domain.repository

import com.example.recipestest.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipeRepository{
    fun getRecipes(): Flow<List<Recipe>>
}