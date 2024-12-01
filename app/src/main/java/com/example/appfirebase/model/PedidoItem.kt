package com.example.appfirebase.model

data class PedidoItem(
    val id: String? = null,
    val nome: String? = null,
    val precoUnitario: Double? = null,
    var quantidade: Int = 1
) {
    fun calcularPrecoTotal(): Double {
        return (precoUnitario ?: 0.0) * quantidade
    }
}
