package com.example.vinheira_agnello_f4

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.vinheira_agnello_f4.data.Produto
import com.example.vinheira_agnello_f4.data.ProdutoDao
import com.example.vinheira_agnello_f4.data.VinheriaDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class ProdutoDaoTest {

    private lateinit var db: VinheriaDatabase
    private lateinit var dao: ProdutoDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            VinheriaDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = db.produtoDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun insertAndReadProduto() = runBlocking {
        val p = Produto(nome = "Test Wine", descricao = "Test", preco = 10.0, quantidade = 5)
        val id = dao.insert(p)
        val all = dao.getAll().first()
        assertEquals(1, all.size)
        assertEquals("Test Wine", all[0].nome)
    }

    @Test
    fun updateProduto() = runBlocking {
        val p = Produto(nome = "Wine", descricao = "D", preco = 5.0, quantidade = 2)
        val id = dao.insert(p)
        val inserted = dao.getById(id)!!
        val updated = inserted.copy(nome = "Wine Updated", preco = 6.0)
        dao.update(updated)
        val fromDb = dao.getById(id)!!
        assertEquals("Wine Updated", fromDb.nome)
        assertEquals(6.0, fromDb.preco, 0.001)
    }

    @Test
    fun deleteProduto() = runBlocking {
        val p = Produto(nome = "ToDelete", descricao = "D", preco = 1.0, quantidade = 1)
        val id = dao.insert(p)
        val inserted = dao.getById(id)!!
        dao.delete(inserted)
        val all = dao.getAll().first()
        assertTrue(all.isEmpty())
    }
}

