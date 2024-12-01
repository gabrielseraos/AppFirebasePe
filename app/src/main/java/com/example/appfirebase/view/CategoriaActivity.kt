package com.example.appfirebase.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appfirebase.controller.CardapioController
import com.example.appfirebase.controller.CategoriaComItensAdapter
import com.example.appfirebase.databinding.ActivityCategoriaBinding
import com.example.appfirebase.model.Categoria
import com.example.appfirebase.model.CategoriaItemModel
import com.google.android.material.tabs.TabLayout

class CategoriaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoriaBinding
    private val controller = CardapioController()
    private lateinit var adapter: CategoriaComItensAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoriaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnVerPedido.setOnClickListener {
            val intent = Intent(this, ResumoPedidoActivity::class.java)
            startActivity(intent)
        }

        // Buscar categorias e itens do controlador
        controller.listarCategorias { categorias ->
            controller.listarItens { itens ->
                // Prepara os dados para o RecyclerView
                val data = controller.prepararDados(categorias, itens)

                // Configura o RecyclerView
                val adapter = CategoriaComItensAdapter(data)
                binding.rvItens.layoutManager = LinearLayoutManager(this)
                binding.rvItens.adapter = adapter

                // Configura o TabLayout
                setupTabs(categorias, data)

                // Sincroniza o RecyclerView com o TabLayout
                setupRecyclerViewScrollSync(categorias, data)
            }
        }
    }


    private fun setupRecyclerViewScrollSync(
        categorias: List<Categoria>,
        data: List<CategoriaItemModel>
    ) {
        binding.rvItens.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()

                // Identifica a categoria visível no RecyclerView
                val categoriaVisivel = data[firstVisiblePosition]
                if (categoriaVisivel is CategoriaItemModel.CategoriaHeader) {
                    val categoriaIndex = categorias.indexOfFirst { it.nome == categoriaVisivel.nomeCategoria }
                    if (categoriaIndex != -1 && binding.tabLayout.selectedTabPosition != categoriaIndex) {
                        binding.tabLayout.selectTab(binding.tabLayout.getTabAt(categoriaIndex))
                    }
                }
            }
        })
    }


    private fun setupTabs(categorias: List<Categoria>, data: List<CategoriaItemModel>) {
        // Adiciona as categorias como abas
        categorias.forEach { categoria ->
            binding.tabLayout.addTab(
                binding.tabLayout.newTab().setText(categoria.nome)
            )
        }

        // Configura o comportamento ao clicar em uma aba
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    val categoriaSelecionada = categorias[it.position].nome
                    // Rola para a posição da categoria no RecyclerView
                    val position = data.indexOfFirst { item ->
                        item is CategoriaItemModel.CategoriaHeader && item.nomeCategoria == categoriaSelecionada
                    }
                    if (position != -1) {
                        (binding.rvItens.layoutManager as LinearLayoutManager)
                            .scrollToPositionWithOffset(position, 0)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }


}
