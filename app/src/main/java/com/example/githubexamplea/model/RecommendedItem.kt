package com.example.githubexamplea.model

data class RecommendedItem(
    val image: String?,
    val title: String,
    val subtitle: String,
    var isFavorite: Boolean = false
)
