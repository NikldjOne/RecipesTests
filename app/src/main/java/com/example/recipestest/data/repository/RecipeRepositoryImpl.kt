package com.example.recipestest.data.repository

import android.util.Log
import com.example.recipestest.data.api.RecipeApi
import com.example.recipestest.data.db.RecipeDao
import com.example.recipestest.data.mapper.toDomain
import com.example.recipestest.data.mapper.toEntity
import com.example.recipestest.domain.model.Recipe
import com.example.recipestest.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

const val SPOONACULAR_API_KEY="02566c05029a4aa291e547828674f441"

class RecipeRepositoryImpl @Inject constructor(
    private val api: RecipeApi,
    private val dao: RecipeDao
) : RecipeRepository {
    override fun getRecipes(): Flow<List<Recipe>> = flow {
        Log.d("Repo", "getRecipes() start - try network")
        try {
            val response = api.getRandomRecipes(SPOONACULAR_API_KEY, number = 20)
            Log.d("Repo", "API success, recipes=${response.recipes.size}")
            val entities = response.recipes.map { it.toEntity() }
            dao.insertRecipes(entities)
            Log.d("Repo", "Inserted into DB: ${entities.size}")
        } catch (e: Exception) {
            Log.e("Repo", "API error", e)
        }
        emitAll(
            dao.getAllRecipes().map { list -> list.map { it.toDomain() } }
        )
    }

}