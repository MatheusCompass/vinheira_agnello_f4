package com.example.vinheira_agnello_f4.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vinheira_agnello_f4.data.Produto
import com.example.vinheira_agnello_f4.viewmodel.ProdutoViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete

@Composable
fun ProductListScreen(
    modifier: Modifier = Modifier,
    viewModel: ProdutoViewModel = hiltViewModel()
) {
    val produtos by viewModel.produtos.collectAsState()

    var nome by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var precoText by remember { mutableStateOf("") }
    var quantidadeText by remember { mutableStateOf("") }
    var editingId by remember { mutableStateOf<Long?>(null) }

    Column(modifier = modifier.padding(16.dp)) {
        OutlinedTextField(value = nome, onValueChange = { nome = it }, label = { Text("Nome") })
        OutlinedTextField(value = descricao, onValueChange = { descricao = it }, label = { Text("Descrição") })
        OutlinedTextField(value = precoText, onValueChange = { precoText = it }, label = { Text("Preço") })
        OutlinedTextField(value = quantidadeText, onValueChange = { quantidadeText = it }, label = { Text("Quantidade") })
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Button(onClick = {
                val preco = precoText.toDoubleOrNull() ?: 0.0
                val quantidade = quantidadeText.toIntOrNull() ?: 0
                if (nome.isNotBlank()) {
                    val produto = Produto(id = editingId ?: 0L, nome = nome, descricao = descricao.ifBlank { null }, preco = preco, quantidade = quantidade)
                    if (editingId == null) {
                        viewModel.insert(produto)
                    } else {
                        viewModel.update(produto)
                        editingId = null
                    }
                    nome = ""; descricao = ""; precoText = ""; quantidadeText = ""
                }
            }) {
                Text(if (editingId == null) "Adicionar" else "Salvar")
            }
            Spacer(modifier = Modifier.width(8.dp))
            if (editingId != null) {
                OutlinedButton(onClick = {
                    editingId = null
                    nome = ""; descricao = ""; precoText = ""; quantidadeText = ""
                }) {
                    Text("Cancelar")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(produtos) { produto ->
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable {
                        // populate the form for editing
                        editingId = produto.id
                        nome = produto.nome
                        descricao = produto.descricao ?: ""
                        precoText = produto.preco.toString()
                        quantidadeText = produto.quantidade.toString()
                    }) {
                    Row(modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text(text = produto.nome, style = MaterialTheme.typography.titleMedium)
                            produto.descricao?.let { Text(text = it, style = MaterialTheme.typography.bodySmall) }
                            Text(text = "R$ ${produto.preco}", style = MaterialTheme.typography.bodySmall)
                        }
                        IconButton(onClick = { viewModel.delete(produto) }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Excluir")
                        }
                    }
                }
            }
        }
    }
}
