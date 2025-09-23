package com.example.recipestest.data.api

import com.example.recipestest.data.model.RecipesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeApi {
    @GET("recipes/random")
    suspend fun getRandomRecipes(
        @Query("apiKey") apiKey: String,
        @Query("number") number: Int = 20
    ): RecipesResponse
}