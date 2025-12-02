package com.example.vinheira_agnello_f4.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import kotlinx.coroutines.launch

@Database(entities = [Produto::class], version = 1, exportSchema = false)
abstract class VinheriaDatabase : RoomDatabase() {
    abstract fun produtoDao(): ProdutoDao

    private class VinheriaDatabaseCallback(
        private val scope: kotlinx.coroutines.CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.produtoDao())
                }
            }
        }

        suspend fun populateDatabase(produtoDao: ProdutoDao) {
            val vinhos = listOf(
                Produto(nome = "Château Ausone Saint-Émilion", preco = 1850.00, quantidade = 12, descricao = "Elegância e complexidade em cada gole. Uva: Merlot. Ano: 2018."),
                Produto(nome = "Barolo Brunate Michele Chiarlo", preco = 420.00, quantidade = 8, descricao = "O rei dos vinhos italianos. Uva: Nebbiolo. Ano: 2017."),
                Produto(nome = "Chablis Premier Cru Montmains", preco = 180.00, quantidade = 15, descricao = "Mineralidade única de Chablis. Uva: Chardonnay. Ano: 2020."),
                Produto(nome = "Dom Pérignon Vintage", preco = 950.00, quantidade = 6, descricao = "O champagne dos sonhos. Uva: Chardonnay. Ano: 2013."),
                Produto(nome = "Quinta do Vale Meão", preco = 280.00, quantidade = 20, descricao = "Tradição portuguesa premium. Uva: Touriga Nacional. Ano: 2019."),
                Produto(nome = "Catena Zapata Malbec", preco = 150.00, quantidade = 25, descricao = "Intensidade argentina autêntica. Uva: Malbec. Ano: 2020."),
                Produto(nome = "Miolo Lote 43 Pinot Noir", preco = 85.00, quantidade = 30, descricao = "Elegância brasileira da Serra Gaúcha. Uva: Pinot Noir. Ano: 2021."),
                Produto(nome = "Whispering Angel Rosé", preco = 120.00, quantidade = 18, descricao = "Frescor provençal incomparável. Uva: Grenache. Ano: 2022."),
                Produto(nome = "Riesling Kabinett Dr. Loosen", preco = 95.00, quantidade = 22, descricao = "Doçura equilibrada alemã. Uva: Riesling. Ano: 2021."),
                Produto(nome = "Almaviva Premium", preco = 380.00, quantidade = 10, descricao = "Joint venture franco-chilena. Uva: Cabernet Sauvignon. Ano: 2018.")
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
                .addCallback(VinheriaDatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

