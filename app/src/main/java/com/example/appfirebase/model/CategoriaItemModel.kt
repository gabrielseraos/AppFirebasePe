package com.example.appfirebase.model

sealed class CategoriaItemModel {
    data class CategoriaHeader(val nomeCategoria: String) : CategoriaItemModel()
    data class ItemContent(
        val id: String?,
        val nome: String?,
        val descricao: String?,
        val preco: Double?
    ) : CategoriaItemModel()
}






















