package com.example.appfirebase.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appfirebase.databinding.ActivityDetalheItemBinding
import com.example.appfirebase.model.Item
import com.google.firebase.firestore.FirebaseFirestore

class DetalheItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalheItemBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalheItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar a Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Detalhes do Item"
        binding.toolbar.setNavigationOnClickListener {
            finish() // Voltar para a tela anterior
        }

        val itemId = intent.getStringExtra("itemId")
        if (itemId != null) {
            carregarDetalhesDoItem(itemId)
        } else {
            Toast.makeText(this, "Erro ao carregar os detalhes do item", Toast.LENGTH_LONG).show()
            finish()
        }

        binding.btnAdicionarAoPedido.setOnClickListener {
            itemId?.let { id -> adicionarOuAtualizarPedido(id) }
        }
    }

    private fun carregarDetalhesDoItem(itemId: String) {
        db.collection("itens").document(itemId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val item = Item(
                        id = document.id,
                        categoriaId = document.getString("categoriaId"),
                        nome = document.getString("nome"),
                        descricao = document.getString("descricao"),
                        preco = document.getDouble("preco"),
                        imagem = document.getString("imagem")
                    )

                    exibirDetalhesDoItem(item)
                } else {
                    Toast.makeText(this, "Item não encontrado", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao carregar detalhes do item", Toast.LENGTH_LONG).show()
                finish()
            }
    }

    private fun exibirDetalhesDoItem(item: Item) {
        binding.txtNomeItem.text = item.nome
        binding.txtDescricaoItem.text = item.descricao
        binding.txtPrecoItem.text = "R$ %.2f".format(item.preco ?: 0.0)

        // Carregar imagem (opcional, se disponível no Firestore)
        // Glide.with(this).load(item.imagem).placeholder(android.R.drawable.ic_menu_gallery).into(binding.imgDetalheItem)
    }

    private fun adicionarOuAtualizarPedido(itemId: String) {
        db.collection("pedidos").document("pedido_atual").get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // Verificar se o item já está no pedido
                    val itens = document.get("itens") as? MutableList<Map<String, Any>> ?: mutableListOf()
                    val itemIndex = itens.indexOfFirst { it["id"] == itemId }

                    if (itemIndex != -1) {
                        // Atualizar a quantidade do item existente
                        val item = itens[itemIndex].toMutableMap()
                        val quantidadeAtual = (item["quantidade"] as? Long ?: 0) + 1
                        item["quantidade"] = quantidadeAtual
                        itens[itemIndex] = item
                    } else {
                        // Adicionar novo item ao pedido
                        db.collection("itens").document(itemId).get()
                            .addOnSuccessListener { itemDoc ->
                                if (itemDoc.exists()) {
                                    val novoItem = mapOf(
                                        "id" to itemDoc.id,
                                        "nome" to (itemDoc.getString("nome") ?: ""),
                                        "precoUnitario" to (itemDoc.getDouble("preco") ?: 0.0),
                                        "quantidade" to 1L
                                    )
                                    itens.add(novoItem)
                                    salvarPedido(itens)
                                }
                            }
                    }
                } else {
                    // Criar um novo pedido com o primeiro item
                    db.collection("itens").document(itemId).get()
                        .addOnSuccessListener { itemDoc ->
                            if (itemDoc.exists()) {
                                val novoPedido = mapOf(
                                    "itens" to listOf(
                                        mapOf(
                                            "id" to itemDoc.id,
                                            "nome" to (itemDoc.getString("nome") ?: ""),
                                            "precoUnitario" to (itemDoc.getDouble("preco") ?: 0.0),
                                            "quantidade" to 1L
                                        )
                                    )
                                )
                                db.collection("pedidos").document("pedido_atual")
                                    .set(novoPedido)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "Pedido criado com sucesso!", Toast.LENGTH_SHORT).show()
                                        finish()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(this, "Erro ao criar o pedido.", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        }
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao acessar o pedido.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun salvarPedido(itens: MutableList<Map<String, Any>>) {
        db.collection("pedidos").document("pedido_atual")
            .update("itens", itens)
            .addOnSuccessListener {
                Toast.makeText(this, "Pedido atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao atualizar o pedido.", Toast.LENGTH_SHORT).show()
            }
    }
}
