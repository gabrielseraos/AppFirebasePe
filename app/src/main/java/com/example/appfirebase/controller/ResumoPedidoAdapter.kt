package com.example.appfirebase.controller

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appfirebase.databinding.ItemResumoPedidoBinding
import com.example.appfirebase.model.PedidoItem

class ResumoPedidoAdapter(
    private val itens: MutableList<PedidoItem>,
    private val onQuantidadeAlterada: () -> Unit,
    private val onItemRemovido: (PedidoItem) -> Unit
) : RecyclerView.Adapter<ResumoPedidoAdapter.PedidoViewHolder>() {

    inner class PedidoViewHolder(private val binding: ItemResumoPedidoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PedidoItem) {
            binding.txtNomeItem.text = item.nome
            binding.txtPrecoUnitario.text = "R$ %.2f".format(item.precoUnitario)
            binding.txtPrecoTotal.text = "R$ %.2f".format(item.calcularPrecoTotal())
            binding.txtQuantidade.text = item.quantidade.toString()

            binding.btnAdicionar.setOnClickListener {
                item.quantidade++
                onQuantidadeAlterada()
                notifyItemChanged(adapterPosition)
            }

            binding.btnRemover.setOnClickListener {
                if (item.quantidade > 1) {
                    item.quantidade--
                    onQuantidadeAlterada()
                    notifyItemChanged(adapterPosition)
                }
            }

            binding.btnExcluir.setOnClickListener {
                onItemRemovido(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoViewHolder {
        val binding = ItemResumoPedidoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PedidoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {
        holder.bind(itens[position])
    }

    override fun getItemCount(): Int = itens.size
}

