package com.example.githubuser.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey
    val id: Int,
    val login: String,
    val avatarUrl: String,
    val isStar: Boolean
)