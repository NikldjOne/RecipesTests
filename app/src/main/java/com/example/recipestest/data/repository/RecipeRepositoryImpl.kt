package com.example.recipestest.data.repository

import android.util.Log
import com.example.recipestest.data.api.RecipeApi
import com.example.recipestest.data.db.RecipeDao
import com.example.recipestest.data.mapper.toDetailsEntity
import com.example.recipestest.data.mapper.toDomain
import com.example.recipestest.data.mapper.toDto
import com.example.recipestest.data.mapper.toEntity
import com.example.recipestest.data.mapper.toIngredientEntities
import com.example.recipestest.data.model.RecipeInformationDto
import com.example.recipestest.domain.model.Recipe
import com.example.recipestest.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

const val SPOONACULAR_API_KEY="02566c05029a4aa291e547828674f441"
const val SPOONACULAR_API_KEY2="6cb9919a70144c96a12c7e6b83001be1"

class RecipeRepositoryImpl @Inject constructor(
    private val api: RecipeApi,
    private val dao: RecipeDao
) : RecipeRepository {
    override fun getRecipes(): Flow<List<Recipe>> = flow {
        Log.d("Repo", "getRecipes() start - try network")
        try {
            val response = api.getRandomRecipes(SPOONACULAR_API_KEY2, number = 20)
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

    override suspend fun getRecipeInformation(id: Int): RecipeInformationDto {
        val cachedDetails = dao.getRecipeDetailsById(id)
        if (cachedDetails != null) {
            val cachedIngredients = dao.getIngredientsForRecipe(id)
            Log.d("Repo", "Cache hit for recipe id=$id")
            return cachedDetails.toDto(cachedIngredients)
        }

        return try {
            Log.d("Repo", "Cache miss, fetching from API id=$id")
            val response = api.getRecipeInformation(id)
            dao.insertRecipeDetails(response.toDetailsEntity())
            dao.insertIngredients(response.toIngredientEntities())
            response
        } catch (e: Exception) {
            Log.e("Repo", "API error for id=$id", e)
            throw e
        }
    }
    override fun searchRecipes(query: String): Flow<List<Recipe>> = flow {
        try {
            val response = api.searchRecipes(query = query)
            emit(response.results.map { it.toDomain() })
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

}