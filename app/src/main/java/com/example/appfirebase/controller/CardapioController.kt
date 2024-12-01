package com.example.appfirebase.controller

import com.example.appfirebase.model.Categoria
import com.example.appfirebase.model.CategoriaItemModel
import com.example.appfirebase.model.Item
import com.google.firebase.firestore.FirebaseFirestore

class CardapioController {

    private val db = FirebaseFirestore.getInstance()

    // Método para listar categorias
    fun listarCategorias(onResult: (List<Categoria>) -> Unit) {
        db.collection("categorias")
            .get()
            .addOnSuccessListener { snapshot ->
                println("Categorias carregadas: ${snapshot.documents.size}")
                val categorias = snapshot.map { document ->
                    Categoria(
                        id = document.id,
                        nome = document.getString("nome")
                    )
                }
                onResult(categorias)
            }
            .addOnFailureListener { exception ->
                println("Erro ao listar categorias: ${exception.message}")
                onResult(emptyList()) // Retorna lista vazia em caso de falha
            }
    }

    // Método para listar itens de uma categoria
    fun listarItensPorCategoria(categoriaId: String, onResult: (List<Item>) -> Unit) {
        db.collection("itens")
            .whereEqualTo("categoriaId", categoriaId)
            .get()
            .addOnSuccessListener { snapshot ->
                val itens = snapshot.map { document ->
                    Item(
                        id = document.id,
                        categoriaId = document.getString("categoriaId"),
                        nome = document.getString("nome"),
                        descricao = document.getString("descricao"),
                        preco = document.getDouble("preco"),
                        imagem = document.getString("imagem")
                    )
                }
                onResult(itens)
            }
            .addOnFailureListener { exception ->
                println("Erro ao listar itens por categoria: ${exception.message}")
                onResult(emptyList()) // Retorna lista vazia em caso de erro
            }
    }
    fun listarItens(onResult: (List<Item>) -> Unit) {
        db.collection("itens")
            .get()
            .addOnSuccessListener { snapshot ->
                val itens = snapshot.map { document ->
                    Item(
                        id = document.id,
                        categoriaId = document.getString("categoriaId"),
                        nome = document.getString("nome"),
                        descricao = document.getString("descricao"),
                        preco = document.getDouble("preco"),
                        imagem = document.getString("imagem")
                    )
                }
                onResult(itens)
            }
            .addOnFailureListener { exception ->
                println("Erro ao listar itens: ${exception.message}")
                onResult(emptyList()) // Retorna lista vazia em caso de erro
            }
    }
    fun prepararDados(
        categorias: List<Categoria>,
        itens: List<Item>
    ): List<CategoriaItemModel> {
        val data = mutableListOf<CategoriaItemModel>()

        categorias.forEach { categoria ->
            // Adiciona o cabeçalho da categoria
            data.add(CategoriaItemModel.CategoriaHeader(categoria.nome ?: ""))

            // Adiciona os itens pertencentes à categoria
            val itensDaCategoria = itens.filter { it.categoriaId == categoria.id }
            itensDaCategoria.forEach { item ->
                data.add(
                    CategoriaItemModel.ItemContent(
                        id = item.id,
                        nome = item.nome,
                        descricao = item.descricao,
                        preco = item.preco
                    )
                )
            }
        }

        return data
    }


}
