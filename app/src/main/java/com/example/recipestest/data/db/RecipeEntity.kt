package com.example.recipestest.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val image: String?,
    val summary: String?,
    val tags: String? = null
)

@Entity(tableName = "recipe_details")
data class RecipeDetailsEntity(
    @PrimaryKey val id: Int,
    val title: String?,
    val image: String?,
    val summary: String?,
    val readyInMinutes: Int?,
    val servings: Int?,
    val instructions: String?
)

@Entity(tableName = "ingredients")
data class IngredientEntity(
    @PrimaryKey(autoGenerate = true) val localId: Int = 0,
    val recipeId: Int,  // foreign key to RecipeDetailsEntity
    val ingredientId: Int?,
    val name: String?,
    val original: String?
)
