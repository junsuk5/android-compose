package com.example.todolist.data.room

import androidx.room.*
import com.example.todolist.data.Todo
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Query("SELECT * FROM todo ORDER BY date DESC")
    fun todos(): Flow<List<Todo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: Todo)

    @Update
    suspend fun update(entity: Todo)

    @Delete
    suspend fun delete(entity: Todo)
}