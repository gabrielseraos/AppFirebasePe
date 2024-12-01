package com.example.appfirebase.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appfirebase.databinding.ItemCardapioBinding
import com.example.appfirebase.model.Item

class ItemAdapter(
    private val itens: List<Item>,
    private val onClick: (Item) -> Unit
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(val binding: ItemCardapioBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemCardapioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itens[position]
        holder.binding.txtNome.text = item.nome
        holder.binding.txtDescricao.text = item.descricao
        holder.binding.txtPreco.text = "US$ ${item.preco}"


        holder.binding.btnAdicionar.setOnClickListener { onClick(item) }
    }

    override fun getItemCount(): Int = itens.size
}
