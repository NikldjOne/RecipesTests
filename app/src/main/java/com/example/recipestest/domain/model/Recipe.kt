package com.example.recipestest.domain.model

data class Recipe(
    val id: Int,
    val title: String,
    val image: String?,
    val summary: String?
)