package com.example.recipestest.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        RecipeEntity::class,
        RecipeDetailsEntity::class,
        IngredientEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
}
