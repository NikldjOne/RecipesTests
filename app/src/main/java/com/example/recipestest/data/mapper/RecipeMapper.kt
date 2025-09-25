package com.example.recipestest.data.mapper

import com.example.recipestest.data.db.IngredientEntity
import com.example.recipestest.data.db.RecipeDetailsEntity
import com.example.recipestest.data.db.RecipeEntity
import com.example.recipestest.data.model.IngredientDto
import com.example.recipestest.data.model.RecipeDto
import com.example.recipestest.data.model.RecipeInformationDto
import com.example.recipestest.domain.model.Recipe

fun RecipeDto.toEntity(tags: String? = null) = RecipeEntity(
    id = id,
    title = title,
    image = image,
    summary = summary,
    tags = tags
)
fun RecipeEntity.toDomain() = Recipe(id, title, image, summary)
fun RecipeDto.toDomain() = Recipe(id, title, image, summary)
fun RecipeInformationDto.toDetailsEntity() = RecipeDetailsEntity(
    id = id,
    title = title,
    image = image,
    summary = summary,
    readyInMinutes = readyInMinutes,
    servings = servings,
    instructions = instructions
)

fun RecipeInformationDto.toIngredientEntities(): List<IngredientEntity> =
    extendedIngredients?.map {
        IngredientEntity(
            recipeId = id,
            ingredientId = it.id,
            name = it.name,
            original = it.original
        )
    } ?: emptyList()

fun RecipeDetailsEntity.toDto(ingredients: List<IngredientEntity>) =
    RecipeInformationDto(
        id = id,
        title = title,
        image = image,
        summary = summary,
        readyInMinutes = readyInMinutes,
        servings = servings,
        instructions = instructions,
        extendedIngredients = ingredients.map {
            IngredientDto(
                id = it.ingredientId,
                name = it.name,
                original = it.original
            )
        }
    )
