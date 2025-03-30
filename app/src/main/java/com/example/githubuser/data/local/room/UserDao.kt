package com.example.githubuser.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.githubuser.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user_table ORDER BY login ASC")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("SELECT * FROM user_table WHERE isStar = 1 ORDER BY isStar DESC")
    fun getStarUsers(): Flow<List<UserEntity>>

    @Query("SELECT EXISTS(SELECT * FROM user_table WHERE id = :userId AND isStar = 1)")
    suspend fun isUserStarred(userId: Int): Boolean

    @Query("SELECT * FROM user_table WHERE id = :userId LIMIT 1")
    suspend fun getUserById(userId: Int): UserEntity?
}