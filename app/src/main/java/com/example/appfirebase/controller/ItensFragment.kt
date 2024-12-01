package com.example.appfirebase.controller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appfirebase.databinding.FragmentItensBinding
import com.example.appfirebase.view.adapter.ItemAdapter

class ItensFragment : Fragment() {

    private var categoriaId: String? = null
    private lateinit var binding: FragmentItensBinding
    private val controller = CardapioController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoriaId = arguments?.getString(ARG_CATEGORIA_ID)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentItensBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurar RecyclerView
        binding.rvItens.layoutManager = LinearLayoutManager(requireContext())

        // Carregar itens da categoria
        categoriaId?.let { id ->
            controller.listarItensPorCategoria(id) { itens ->
                val adapter = ItemAdapter(itens) { item ->
                    // Ação ao clicar em um item (por exemplo, abrir detalhes)
                }
                binding.rvItens.adapter = adapter
            }
        }
    }

    companion object {
        private const val ARG_CATEGORIA_ID = "categoriaId"

        fun newInstance(categoriaId: String): ItensFragment {
            val fragment = ItensFragment()
            val args = Bundle()
            args.putString(ARG_CATEGORIA_ID, categoriaId)
            fragment.arguments = args
            return fragment
        }
    }
}
