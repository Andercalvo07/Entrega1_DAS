package com.example.migym.model

data class Workout(
    val id: String,
    val title: String,
    val description: String,
    val type: String,
    val day: String,
    val hour: String,
    val duration: Int
) 