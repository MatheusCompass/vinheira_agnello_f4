package com.example.vinheira_agnello_f4.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import kotlinx.coroutines.launch

@Database(entities = [Produto::class], version = 2, exportSchema = false)
abstract class VinheriaDatabase : RoomDatabase() {
    abstract fun produtoDao(): ProdutoDao

    private class VinheriaDatabaseCallback(
        private val scope: kotlinx.coroutines.CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
            super.onCreate(db)
        }

        override fun onOpen(db: androidx.sqlite.db.SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val count = database.produtoDao().getCount()
                    if (count == 0) {
                        populateDatabase(database.produtoDao())
                    }
                }
            }
        }

        suspend fun populateDatabase(produtoDao: ProdutoDao) {
            val vinhos = listOf(
                Produto(nome = "Château Ausone Saint-Émilion", preco = 1850.00, quantidade = 12, descricao = "Elegância e complexidade em cada gole.", uva = "Merlot", ano = 2018),
                Produto(nome = "Barolo Brunate Michele Chiarlo", preco = 420.00, quantidade = 8, descricao = "O rei dos vinhos italianos.", uva = "Nebbiolo", ano = 2017),
                Produto(nome = "Chablis Premier Cru Montmains", preco = 180.00, quantidade = 15, descricao = "Mineralidade única de Chablis.", uva = "Chardonnay", ano = 2020),
                Produto(nome = "Dom Pérignon Vintage", preco = 950.00, quantidade = 6, descricao = "O champagne dos sonhos.", uva = "Chardonnay", ano = 2013),
                Produto(nome = "Quinta do Vale Meão", preco = 280.00, quantidade = 20, descricao = "Tradição portuguesa premium.", uva = "Touriga Nacional", ano = 2019),
                Produto(nome = "Catena Zapata Malbec", preco = 150.00, quantidade = 25, descricao = "Intensidade argentina autêntica.", uva = "Malbec", ano = 2020),
                Produto(nome = "Miolo Lote 43 Pinot Noir", preco = 85.00, quantidade = 30, descricao = "Elegância brasileira da Serra Gaúcha.", uva = "Pinot Noir", ano = 2021),
                Produto(nome = "Whispering Angel Rosé", preco = 120.00, quantidade = 18, descricao = "Frescor provençal incomparável.", uva = "Grenache", ano = 2022),
                Produto(nome = "Riesling Kabinett Dr. Loosen", preco = 95.00, quantidade = 22, descricao = "Doçura equilibrada alemã.", uva = "Riesling", ano = 2021),
                Produto(nome = "Almaviva Premium", preco = 380.00, quantidade = 10, descricao = "Joint venture franco-chilena.", uva = "Cabernet Sauvignon", ano = 2018)
            )
            produtoDao.insertAll(vinhos)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: VinheriaDatabase? = null

        fun getInstance(context: Context, scope: kotlinx.coroutines.CoroutineScope): VinheriaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VinheriaDatabase::class.java,
                    "vinheria_database"
                )
                .fallbackToDestructiveMigration()
                .addCallback(VinheriaDatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

