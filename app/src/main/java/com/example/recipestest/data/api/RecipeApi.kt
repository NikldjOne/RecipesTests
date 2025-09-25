package com.example.recipestest.data.api

import com.example.recipestest.data.model.RecipeInformationDto
import com.example.recipestest.data.model.RecipesResponse
import com.example.recipestest.data.model.SearchResponse
import com.example.recipestest.data.repository.SPOONACULAR_API_KEY
import com.example.recipestest.data.repository.SPOONACULAR_API_KEY2
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApi {
    @GET("recipes/random")
    suspend fun getRandomRecipes(
        @Query("apiKey") apiKey: String,
        @Query("number") number: Int = 20
    ): RecipesResponse

    @GET("recipes/{id}/information")
    suspend fun getRecipeInformation(
        @Path("id") id: Int,
        @Query("includeNutrition") includeNutrition: Boolean = false,
        @Query("apiKey") apiKey: String = SPOONACULAR_API_KEY2,
    ): RecipeInformationDto

    @GET("recipes/complexSearch")
    suspend fun searchRecipes(
        @Query("apiKey") apiKey: String = SPOONACULAR_API_KEY2,
        @Query("query") query: String,
        @Query("number") number: Int = 20,
        @Query("addRecipeInformation") addInfo: Boolean = true
    ): SearchResponse

}