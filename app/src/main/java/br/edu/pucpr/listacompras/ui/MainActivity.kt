package br.edu.pucpr.listacompras.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import br.edu.pucpr.listacompras.R
import br.edu.pucpr.listacompras.databinding.ActivityMainBinding
import br.edu.pucpr.listacompras.db.ItemCompraDao
import br.edu.pucpr.listacompras.model.ItemCompra
import br.edu.pucpr.listacompras.repository.ComprasRepository
import br.edu.pucpr.listacompras.util.gone
import br.edu.pucpr.listacompras.util.visible
import br.edu.pucpr.listacompras.util.visibleIf
import br.edu.pucpr.listacompras.util.toShareText
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Main Activity - displays the shopping list with category, share, and dashboard support
 */
class MainActivity : AppCompatActivity(), ItemFormDialog.OnItemSavedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ComprasViewModel
    private lateinit var adapter: ComprasAdapter

    private var pendingDeleteItem: ItemCompra? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDatabase()
        setupRecyclerView()
        setupFab()
        setupEmptyState()
        setupSwipeToDelete()
        setupShareButton()
        setupDashboardButton()
        observeViewModel()
    }

    private fun setupDatabase() {
        val database = br.edu.pucpr.listacompras.data.AppDatabase.getInstance(this)
        val dao = database.itemCompraDao()
        val repository = ComprasRepository(dao)
        val factory = ComprasViewModelFactory(repository)
        viewModel = androidx.lifecycle.ViewModelProvider(this, factory)[ComprasViewModel::class.java]
    }

    private fun setupRecyclerView() {
        adapter = ComprasAdapter(
            onItemClick = { item -> openEditDialog(item) },
            onCheckChanged = { item -> viewModel.toggleItemStatus(item) },
            onDeleteClick = { item -> confirmDelete(item) }
        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }
    }

    private fun setupFab() {
        binding.fabAddItem.setOnClickListener {
            openAddDialog()
        }
        binding.emptyStateButton.setOnClickListener {
            openAddDialog()
        }
    }

    private fun setupEmptyState() {
        // Empty state visibility is handled in observeViewModel()
    }

    private fun setupSwipeToDelete() {
        val swipeHandler = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val item = adapter.currentList.getOrNull(position)
                item?.let { confirmDelete(it) }
            }
        }

        ItemTouchHelper(swipeHandler).attachToRecyclerView(binding.recyclerView)
    }

    private fun setupShareButton() {
        binding.buttonShare.setOnClickListener {
            shareList()
        }
    }

    private fun setupDashboardButton() {
        binding.buttonDashboard.setOnClickListener {
            openDashboard()
        }
    }

    private fun openDashboard() {
        val total = viewModel.totalCount.value
        val comprado = viewModel.compradoCount.value
        val pendente = viewModel.pendenteCount.value
        val categories = viewModel.countByCategoria.value

        val breakdown = categories.joinToString("\n") { cat ->
            "• ${cat.categoria}: ${cat.count}"
        }

        val dialog = DashboardDialog.newInstance(total, comprado, pendente, breakdown)
        dialog.show(supportFragmentManager, "DashboardDialog")
    }

    private fun shareList() {
        val items = viewModel.items.value
        if (items.isNullOrEmpty()) {
            Snackbar.make(binding.root, R.string.snackbar_empty_list, Snackbar.LENGTH_SHORT).show()
            return
        }

        val shareText = items.toShareText()
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject))
            type = "text/plain"
        }
        startActivity(Intent.createChooser(sendIntent, getString(R.string.share_chooser_title)))
    }

    private fun observeViewModel() {
        lifecycleScope.launchWhenStarted {
            viewModel.items.collectLatest { items ->
                adapter.submitList(items)
                binding.emptyStateLayout.visibleIf(items.isEmpty())
                binding.recyclerView.visibleIf(items.isNotEmpty())
            }
        }

        // Collect statistics flows so they stay warm for the dashboard
        lifecycleScope.launchWhenStarted {
            viewModel.totalCount.collectLatest { /* collected to keep flow warm */ }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.compradoCount.collectLatest { /* collected to keep flow warm */ }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.pendenteCount.collectLatest { /* collected to keep flow warm */ }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.countByCategoria.collectLatest { /* collected to keep flow warm */ }
        }

        viewModel.event.observe(this) { event ->
            event?.getContent()?.let { uiEvent ->
                handleUiEvent(uiEvent)
                viewModel.onEventHandled()
            }
        }
    }

    private fun handleUiEvent(event: ComprasViewModel.UiEvent) {
        when (event) {
            is ComprasViewModel.UiEvent.ItemAdded -> {
                showSnackbar(R.string.snackbar_item_added)
            }
            is ComprasViewModel.UiEvent.ItemUpdated -> {
                showSnackbar(R.string.snackbar_item_updated)
            }
            is ComprasViewModel.UiEvent.ItemDeleted -> {
                pendingDeleteItem = event.item
                showDeleteSnackbar()
            }
            is ComprasViewModel.UiEvent.ItemRestored -> {
                showSnackbar(R.string.snackbar_item_added)
                pendingDeleteItem = null
            }
        }
    }

    private fun showSnackbar(messageResId: Int) {
        Snackbar.make(binding.root, messageResId, Snackbar.LENGTH_SHORT).show()
    }

    private fun showDeleteSnackbar() {
        Snackbar.make(binding.root, R.string.snackbar_item_deleted, Snackbar.LENGTH_LONG)
            .setAction(R.string.snackbar_undo) {
                pendingDeleteItem?.let { item ->
                    viewModel.undoDelete(item)
                }
            }
            .setActionTextColor(getColor(R.color.md_theme_light_primary))
            .show()
    }

    private fun openAddDialog() {
        val dialog = ItemFormDialog.newInstanceAdd()
        dialog.setListener(this)
        dialog.show(supportFragmentManager, "ItemFormDialog")
    }

    private fun openEditDialog(item: ItemCompra) {
        val dialog = ItemFormDialog.newInstanceEdit(item)
        dialog.setListener(this)
        dialog.show(supportFragmentManager, "ItemFormDialog")
    }

    private fun confirmDelete(item: ItemCompra) {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_title_confirm_delete)
            .setMessage(R.string.dialog_message_confirm_delete)
            .setPositiveButton(R.string.button_delete) { _, _ ->
                viewModel.deleteItem(item)
            }
            .setNegativeButton(R.string.button_cancel, null)
            .show()
    }

    override fun onItemSaved(nome: String, quantidade: String, categoria: String, itemToEdit: ItemCompra?) {
        if (itemToEdit != null) {
            viewModel.updateItem(itemToEdit, nome, quantidade, categoria)
        } else {
            viewModel.addItem(nome, quantidade, categoria)
        }
    }
}
