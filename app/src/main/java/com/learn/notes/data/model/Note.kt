package com.learn.notes.data.model

data class Note(
    val id: Long = 0,
    var title: String,
    var content: String,
    val timestamp: Long
)