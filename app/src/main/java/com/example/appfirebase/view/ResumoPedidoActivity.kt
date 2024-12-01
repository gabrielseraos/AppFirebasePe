package com.example.appfirebase.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appfirebase.databinding.ActivityResumoPedidoBinding
import com.example.appfirebase.model.PedidoItem
import com.example.appfirebase.controller.ResumoPedidoAdapter
import com.google.firebase.firestore.FirebaseFirestore

class ResumoPedidoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResumoPedidoBinding
    private val db = FirebaseFirestore.getInstance()
    private val itensPedido = mutableListOf<PedidoItem>()
    private lateinit var adapter: ResumoPedidoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResumoPedidoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar RecyclerView
        adapter = ResumoPedidoAdapter(
            itensPedido,
            onQuantidadeAlterada = { atualizarTotal() },
            onItemRemovido = { item -> removerItemPedido(item) }
        )
        binding.rvResumoPedido.layoutManager = LinearLayoutManager(this)
        binding.rvResumoPedido.adapter = adapter

        // Carregar os itens do pedido do Firestore
        carregarItensPedido()

        // Botão Confirmar Pedido
        binding.btnConfirmarPedido.setOnClickListener {
            salvarPedidoNoFirestore()
            Toast.makeText(this, "Pedido confirmado com sucesso!", Toast.LENGTH_LONG).show()
            // Opcional: Redirecionar para uma tela de confirmação
        }

        // Botão Voltar para o Cardápio
        binding.btnVoltarParaCardapio.setOnClickListener {
            salvarPedidoNoFirestore()
            finish() // Retorna ao cardápio
        }
    }

    private fun carregarItensPedido() {
        // Carregar itens do documento "pedido_atual" no Firestore
        db.collection("pedidos").document("pedido_atual").get()
            .addOnSuccessListener { document ->
                itensPedido.clear()
                val itens = document.get("itens") as? List<Map<String, Any>> ?: emptyList()
                for (itemMap in itens) {
                    val item = PedidoItem(
                        id = itemMap["id"] as? String,
                        nome = itemMap["nome"] as? String ?: "",
                        precoUnitario = (itemMap["precoUnitario"] as? Double) ?: 0.0,
                        quantidade = (itemMap["quantidade"] as? Long)?.toInt() ?: 0
                    )
                    if (item.quantidade > 0) { // Evitar itens com quantidade 0
                        itensPedido.add(item)
                    }
                }
                adapter.notifyDataSetChanged()
                atualizarTotal()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao carregar o pedido.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun salvarPedidoNoFirestore() {
        val pedidoAtual = mapOf(
            "itens" to itensPedido.map { item ->
                mapOf(
                    "id" to item.id,
                    "nome" to item.nome,
                    "precoUnitario" to item.precoUnitario,
                    "quantidade" to item.quantidade
                )
            }
        )

        db.collection("pedidos").document("pedido_atual")
            .set(pedidoAtual)
            .addOnSuccessListener {
                Toast.makeText(this, "Pedido salvo com sucesso!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao salvar o pedido.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun removerItemPedido(item: PedidoItem) {
        // Remover da lista local
        itensPedido.remove(item)
        adapter.notifyDataSetChanged()
        atualizarTotal()

        // Atualizar Firestore após remover o item
        salvarPedidoNoFirestore()
    }

    private fun atualizarTotal() {
        val total = itensPedido.sumOf { it.calcularPrecoTotal() }
        binding.txtTotalGeral.text = "Total: R$ %.2f".format(total)
    }
}
