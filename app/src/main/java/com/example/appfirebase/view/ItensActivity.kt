package com.example.appfirebase.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appfirebase.controller.CardapioController
import com.example.appfirebase.databinding.ActivityItensBinding
import com.example.appfirebase.view.adapter.ItemAdapter

class ItensActivity : AppCompatActivity() {

    private lateinit var binding: ActivityItensBinding
    private val controller = CardapioController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItensBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val categoriaId = intent.getStringExtra("categoriaId") ?: return

        controller.listarItensPorCategoria(categoriaId) { itens ->
            val adapter = ItemAdapter(itens) { item ->
                val intent = Intent(this, DetalheItemActivity::class.java)
                intent.putExtra("itemId", item.id)
                startActivity(intent)
            }
            binding.rvItens.layoutManager = LinearLayoutManager(this)
            binding.rvItens.adapter = adapter
        }
    }
}
