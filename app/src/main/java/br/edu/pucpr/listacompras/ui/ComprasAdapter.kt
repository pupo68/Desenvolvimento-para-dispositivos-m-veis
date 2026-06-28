package br.edu.pucpr.listacompras.ui

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.edu.pucpr.listacompras.R
import br.edu.pucpr.listacompras.databinding.ItemCompraBinding
import br.edu.pucpr.listacompras.model.ItemCompra
import br.edu.pucpr.listacompras.util.toAccessibilityDescription

/**
 * RecyclerView Adapter for ItemCompra list
 * Uses ListAdapter with DiffUtil for efficient updates
 */
class ComprasAdapter(
    private val onItemClick: (ItemCompra) -> Unit,
    private val onCheckChanged: (ItemCompra) -> Unit,
    private val onDeleteClick: (ItemCompra) -> Unit
) : ListAdapter<ItemCompra, ComprasAdapter.ComprasViewHolder>(ItemCompraDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComprasViewHolder {
        val binding = ItemCompraBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ComprasViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ComprasViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ComprasViewHolder(
        private val binding: ItemCompraBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ItemCompra) {
            binding.apply {
                textNome.text = item.nome
                textQuantidade.text = item.quantidade
                textCategoria.text = item.categoria
                checkboxComprado.isChecked = item.isComprado

                // Accessibility: card reads as single unit via noHideDescendants + contentDescription
                root.contentDescription = item.toAccessibilityDescription()

                // Accessibility: checkbox contentDescription is contextual (not just "checkbox")
                checkboxComprado.contentDescription = if (item.isComprado) {
                    itemView.context.getString(R.string.checkbox_item_checked_content_description, item.nome)
                } else {
                    itemView.context.getString(R.string.checkbox_item_content_description, item.nome)
                }

                // Apply strikethrough when item is purchased
                if (item.isComprado) {
                    textNome.paintFlags = textNome.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    textNome.alpha = 0.6f
                } else {
                    textNome.paintFlags = textNome.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                    textNome.alpha = 1.0f
                }

                // Click listeners
                root.setOnClickListener { onItemClick(item) }
                checkboxComprado.setOnCheckedChangeListener { _, isChecked ->
                    if (item.isComprado != isChecked) {
                        onCheckChanged(item)
                    }
                }
                buttonDelete.setOnClickListener { onDeleteClick(item) }
            }
        }
    }

    /**
     * DiffUtil callback for efficient list updates
     */
    class ItemCompraDiffCallback : DiffUtil.ItemCallback<ItemCompra>() {
        override fun areItemsTheSame(oldItem: ItemCompra, newItem: ItemCompra): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ItemCompra, newItem: ItemCompra): Boolean {
            return oldItem == newItem
        }
    }
}
