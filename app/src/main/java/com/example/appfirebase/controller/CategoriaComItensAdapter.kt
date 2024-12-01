package com.example.appfirebase.controller

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appfirebase.databinding.ItemCategoriaBinding
import com.example.appfirebase.databinding.ItemCardapioBinding
import com.example.appfirebase.model.CategoriaItemModel
import com.example.appfirebase.view.DetalheItemActivity

class CategoriaComItensAdapter(
    private val data: List<CategoriaItemModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (data[position]) {
            is CategoriaItemModel.CategoriaHeader -> TYPE_HEADER
            is CategoriaItemModel.ItemContent -> TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_HEADER) {
            val binding = ItemCategoriaBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            CategoriaHeaderViewHolder(binding)
        } else {
            val binding = ItemCardapioBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            ItemContentViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CategoriaHeaderViewHolder -> {
                val categoria = data[position] as CategoriaItemModel.CategoriaHeader
                holder.bind(categoria)
            }
            is ItemContentViewHolder -> {
                val item = data[position] as CategoriaItemModel.ItemContent
                holder.bind(item)
            }
        }
    }

    override fun getItemCount(): Int = data.size

    class CategoriaHeaderViewHolder(private val binding: ItemCategoriaBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(header: CategoriaItemModel.CategoriaHeader) {
            binding.txtNomeCategoria.text = header.nomeCategoria
        }
    }

    class ItemContentViewHolder(private val binding: ItemCardapioBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CategoriaItemModel.ItemContent) {
            binding.txtNome.text = item.nome
            binding.txtDescricao.text = item.descricao
            binding.txtPreco.text = "R$ %.2f".format(item.preco ?: 0.0)

            binding.imgItem.setImageResource(android.R.drawable.ic_menu_gallery)
            // Adicionando clique no item para abrir a tela de detalhes
            binding.root.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, DetalheItemActivity::class.java)
                intent.putExtra("itemId", item.id) // Passa o ID do item
                context.startActivity(intent)
            }



























        }
    }
}
