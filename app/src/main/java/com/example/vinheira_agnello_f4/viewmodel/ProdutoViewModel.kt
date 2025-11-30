package com.example.vinheira_agnello_f4.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.vinheira_agnello_f4.data.Produto
import com.example.vinheira_agnello_f4.data.ProdutoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProdutoViewModel @Inject constructor(
    application: Application,
    private val repository: ProdutoRepository,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    val produtos: StateFlow<List<Produto>> = repository.produtos
        .map { it }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun insert(produto: Produto) {
        viewModelScope.launch {
            repository.insert(produto)
        }
    }

    fun update(produto: Produto) {
        viewModelScope.launch {
            repository.update(produto)
        }
    }

    fun delete(produto: Produto) {
        viewModelScope.launch {
            repository.delete(produto)
        }
    }
}
