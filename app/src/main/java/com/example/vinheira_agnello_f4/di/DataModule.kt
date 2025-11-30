package com.example.vinheira_agnello_f4.di

import android.content.Context
import com.example.vinheira_agnello_f4.data.ProdutoDao
import com.example.vinheira_agnello_f4.data.VinheriaDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): VinheriaDatabase {
        return VinheriaDatabase.getInstance(context)
    }

    @Provides
    fun provideProdutoDao(db: VinheriaDatabase): ProdutoDao {
        return db.produtoDao()
    }
}

