package com.example.mystoryapp.data.local.datastore.preferences

data class UserModel(
    val email : String,
    val token : String,
    val isLogin : Boolean = false
)
