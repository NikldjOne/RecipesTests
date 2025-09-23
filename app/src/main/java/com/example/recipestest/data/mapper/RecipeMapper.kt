package com.example.recipestest.data.mapper

import com.example.recipestest.data.db.RecipeEntity
import com.example.recipestest.data.model.RecipeDto
import com.example.recipestest.domain.model.Recipe

fun RecipeDto.toEntity() = RecipeEntity(id, title, image, summary)
fun RecipeEntity.toDomain() = Recipe(id, title, image, summary)