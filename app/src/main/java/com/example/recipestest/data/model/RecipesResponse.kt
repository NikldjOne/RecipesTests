package com.example.recipestest.data.model

data class RecipesResponse(
    val recipes: List<RecipeDto>
)

data class RecipeDto(
    val id: Int,
    val title: String,
    val image: String?,
    val summary: String?
)

data class IngredientDto(
    val id: Int?,
    val name: String?,
    val original: String?
)

data class RecipeInformationDto(
    val id: Int,
    val title: String?,
    val image: String?,
    val summary: String?,
    val readyInMinutes: Int?,
    val servings: Int?,
    val extendedIngredients: List<IngredientDto>? = emptyList(),
    val instructions: String? = null
)

data class SearchResponse(
    val results: List<RecipeDto>,
    val offset: Int,
    val number: Int,
    val totalResults: Int
)
