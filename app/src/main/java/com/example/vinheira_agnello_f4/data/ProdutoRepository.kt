package com.example.vinheira_agnello_f4.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProdutoRepository @Inject constructor(private val produtoDao: ProdutoDao) {

    val produtos: Flow<List<Produto>> = produtoDao.getAll()

    suspend fun insert(produto: Produto): Long {
        return produtoDao.insert(produto)
    }

    suspend fun update(produto: Produto) {
        produtoDao.update(produto)
    }

    suspend fun delete(produto: Produto) {
        produtoDao.delete(produto)
    }
}
