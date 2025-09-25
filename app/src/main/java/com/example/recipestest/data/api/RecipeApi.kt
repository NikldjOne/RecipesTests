package com.example.recipestest.data.api

import com.example.recipestest.data.model.RecipeInformationDto
import com.example.recipestest.data.model.RecipesResponse
import com.example.recipestest.data.model.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

const val SPOONACULAR_API_KEY="02566c05029a4aa291e547828674f441"
const val SPOONACULAR_API_KEY2="6cb9919a70144c96a12c7e6b83001be1"
const val SPOONACULAR_API_KEY3="922049b84c5e443781c9dfe46b3ce6f2"
const val SPOONACULAR_API_KEY4="ab0854eff82740b59ecd452d69294ad5"
const val SPOONACULAR_API_KEY5="0cc584c3730e4b579c6dfdf4cf9c33ce"
const val SPOONACULAR_API_KEY6="616592cb772f43ee902826b7584f90bb"

interface RecipeApi {
    @GET("recipes/random")
    suspend fun getRandomRecipes(
        @Query("apiKey") apiKey: String = SPOONACULAR_API_KEY6,
        @Query("number") number: Int = 20,
        @Query("include-tags") tags: String? = null
    ): RecipesResponse

    @GET("recipes/{id}/information")
    suspend fun getRecipeInformation(
        @Path("id") id: Int,
        @Query("includeNutrition") includeNutrition: Boolean = false,
        @Query("apiKey") apiKey: String = SPOONACULAR_API_KEY6,
    ): RecipeInformationDto

    @GET("recipes/complexSearch")
    suspend fun searchRecipes(
        @Query("apiKey") apiKey: String = SPOONACULAR_API_KEY6,
        @Query("query") query: String,
        @Query("number") number: Int = 20,
        @Query("addRecipeInformation") addInfo: Boolean = true
    ): SearchResponse

}