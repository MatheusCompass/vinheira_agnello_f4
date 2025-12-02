package com.example.vinheira_agnello_f4.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProdutoDao {
    @Query("SELECT * FROM produtos ORDER BY nome ASC")
    fun getAll(): Flow<List<Produto>>

    @Query("SELECT * FROM produtos WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): Produto?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(produto: Produto): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(produtos: List<Produto>)

    @Update
    suspend fun update(produto: Produto)

    @Delete
    suspend fun delete(produto: Produto)
}

