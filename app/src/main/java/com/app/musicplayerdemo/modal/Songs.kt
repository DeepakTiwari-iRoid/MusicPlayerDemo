package com.app.musicplayerdemo.modal

data class Songs(
    val title: String,
    val url: String,
    val imageUrl: String? = null,
    val author: String
)
