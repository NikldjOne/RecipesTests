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
const val SPOONACULAR_API_KEY3="922049b84c5e443781c9dfe46b3ce6f2"
const val SPOONACULAR_API_KEY4="ab0854eff82740b59ecd452d69294ad5"
const val SPOONACULAR_API_KEY5="0cc584c3730e4b579c6dfdf4cf9c33ce"
const val SPOONACULAR_API_KEY6="616592cb772f43ee902826b7584f90bb"

class RecipeRepositoryImpl @Inject constructor(
    private val api: RecipeApi,
    private val dao: RecipeDao
) : RecipeRepository {

    override fun getRecipes(tags: String?): Flow<List<Recipe>> = flow {
        Log.d("Repo", "getRecipes() start - try network")

        val networkRecipes = try {
            val response = api.getRandomRecipes(
                apiKey = SPOONACULAR_API_KEY6,
                number = 20,
                tags = tags
            )
            Log.d("Repo", "API success, recipes=${response.recipes.size}")

            val entities = response.recipes.map { it.toEntity() }
            dao.insertRecipes(entities)
            Log.d("Repo", "Inserted into DB: ${entities.size}")

            response.recipes.map { it.toDomain() }
        } catch (e: Exception) {
            Log.e("Repo", "API error", e)
            emptyList()
        }

        emit(networkRecipes)

        if (networkRecipes.isEmpty()) {
            emitAll(
                dao.getAllRecipes().map { list ->
                    Log.d("Repo", "Emit from DB, size=${list.size}")
                    list.map { it.toDomain() }
                }
            )
        }
    }


    override suspend fun getRecipeInformation(id: Int): RecipeInformationDto {
        return try {
            Log.d("Repo", "Fetching from API id=$id")
            val response = api.getRecipeInformation(id)
            dao.insertRecipeDetails(response.toDetailsEntity())
            dao.insertIngredients(response.toIngredientEntities())
            response
        } catch (e: Exception) {
            Log.e("Repo", "API error for id=$id, fallback to cache", e)
            val cachedDetails = dao.getRecipeDetailsById(id)
            if (cachedDetails != null) {
                val cachedIngredients = dao.getIngredientsForRecipe(id)
                cachedDetails.toDto(cachedIngredients)
            } else {
                throw e
            }
        }
    }

    override fun searchRecipes(query: String): Flow<List<Recipe>> = flow {
        val networkRecipes = try {
            val response = api.searchRecipes(query = query)
            val entities = response.results.map { it.toEntity() }
            dao.insertRecipes(entities)
            response.results.map { it.toDomain() }
        } catch (e: Exception) {
            Log.e("Repo", "API search error", e)
            emptyList()
        }

        emit(networkRecipes)

        if (networkRecipes.isEmpty()) {
            emitAll(
                dao.searchRecipes(query).map { list -> list.map { it.toDomain() } }
            )
        }
    }

}