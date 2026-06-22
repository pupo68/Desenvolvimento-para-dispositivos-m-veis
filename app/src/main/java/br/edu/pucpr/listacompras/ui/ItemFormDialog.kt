package br.edu.pucpr.listacompras.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import br.edu.pucpr.listacompras.R
import br.edu.pucpr.listacompras.databinding.DialogItemFormBinding
import br.edu.pucpr.listacompras.model.ItemCompra

/**
 * BottomSheetDialogFragment for adding/editing items
 */
class ItemFormDialog : BottomSheetDialogFragment() {

    private var _binding: DialogItemFormBinding? = null
    private val binding get() = _binding!!

    private lateinit var item: ItemCompra
    private var isEditMode = false

    companion object {
        private const val ARG_ITEM_ID = "arg_item_id"
        private const val ARG_ITEM_NOME = "arg_item_nome"
        private const val ARG_ITEM_QUANTIDADE = "arg_item_quantidade"
        private const val ARG_ITEM_COMPRADO = "arg_item_comprado"
        private const val ARG_ITEM = "arg_item"
        private const val ARG_IS_EDIT_MODE = "arg_is_edit_mode"

        /**
         * Factory method to create new instance for adding item
         */
        fun newInstanceAdd(): ItemFormDialog {
            return ItemFormDialog().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_IS_EDIT_MODE, false)
                }
            }
        }

        /**
         * Factory method to create new instance for editing item
         */
        fun newInstanceEdit(item: ItemCompra): ItemFormDialog {
            return ItemFormDialog().apply {
                arguments = Bundle().apply {
                    putInt(ARG_ITEM_ID, item.id)
                    putString(ARG_ITEM_NOME, item.nome)
                    putString(ARG_ITEM_QUANTIDADE, item.quantidade)
                    putBoolean(ARG_ITEM_COMPRADO, item.isComprado)
                    putBoolean(ARG_IS_EDIT_MODE, true)
                }
            }
        }
    }

    interface OnItemSavedListener {
        fun onItemSaved(nome: String, quantidade: String, itemToEdit: ItemCompra?)
    }

    private var listener: OnItemSavedListener? = null

    fun setListener(listener: OnItemSavedListener) {
        this.listener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isEditMode = it.getBoolean(ItemFormDialog.ARG_IS_EDIT_MODE, false)
            item = if (isEditMode) {
                ItemCompra(
                    id = it.getInt(ItemFormDialog.ARG_ITEM_ID, 0),
                    nome = it.getString(ItemFormDialog.ARG_ITEM_NOME) ?: "",
                    quantidade = it.getString(ItemFormDialog.ARG_ITEM_QUANTIDADE) ?: "",
                    isComprado = it.getBoolean(ItemFormDialog.ARG_ITEM_COMPRADO, false)
                )
            } else {
                ItemCompra(nome = "", quantidade = "")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogItemFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set title based on mode
        binding.dialogTitle.text = if (isEditMode) {
            getString(R.string.dialog_title_edit_item)
        } else {
            getString(R.string.dialog_title_add_item)
        }

        // Pre-fill fields in edit mode
        if (isEditMode) {
            binding.editNome.setText(item.nome)
            binding.editQuantidade.setText(item.quantidade)
        }

        // Validation - enable/disable save button
        setupValidation()

        // Save button
        binding.buttonSalvar.setOnClickListener {
            val nome = binding.editNome.text?.toString()?.trim() ?: ""
            val quantidade = binding.editQuantidade.text?.toString()?.trim() ?: ""

            if (validateFields(nome, quantidade)) {
                listener?.onItemSaved(nome, quantidade, if (isEditMode) item else null)
                dismiss()
            }
        }

        // Cancel button
        binding.buttonCancelar.setOnClickListener {
            dismiss()
        }

        // Auto-focus on nome field and show keyboard
        binding.editNome.requestFocus()
        binding.editNome.postDelayed({
            showKeyboard()
        }, 100)
    }

    private fun setupValidation() {
        binding.editNome.doAfterTextChanged {
            validateFields(
                binding.editNome.text?.toString()?.trim() ?: "",
                binding.editQuantidade.text?.toString()?.trim() ?: ""
            )
        }

        binding.editQuantidade.doAfterTextChanged {
            validateFields(
                binding.editNome.text?.toString()?.trim() ?: "",
                binding.editQuantidade.text?.toString()?.trim() ?: ""
            )
        }
    }

    private fun validateFields(nome: String, quantidade: String): Boolean {
        val isValid = nome.isNotBlank() && quantidade.isNotBlank()

        binding.textInputNome.error = if (nome.isBlank()) {
            getString(R.string.item_name_error)
        } else {
            null
        }

        binding.textInputQuantidade.error = if (quantidade.isBlank()) {
            getString(R.string.item_quantity_error)
        } else {
            null
        }

        binding.buttonSalvar.isEnabled = isValid
        return isValid
    }

    private fun showKeyboard() {
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}