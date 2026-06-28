package br.edu.pucpr.listacompras.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.pucpr.listacompras.db.ItemCompraDao
import br.edu.pucpr.listacompras.model.ItemCompra
import br.edu.pucpr.listacompras.repository.ComprasRepository
import br.edu.pucpr.listacompras.util.toTitleCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel for Shopping List app
 * Manages UI state and handles CRUD operations via Repository
 */
class ComprasViewModel(private val repository: ComprasRepository) : ViewModel() {

    /**
     * StateFlow with list of items, ordered by isComprado (false first) then by name
     */
    val items: StateFlow<List<ItemCompra>> = repository.allItems
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /** Statistics flows for dashboard */
    val totalCount: StateFlow<Int> = repository.totalCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val compradoCount: StateFlow<Int> = repository.compradoCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val pendenteCount: StateFlow<Int> = repository.pendenteCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val countByCategoria: StateFlow<List<ItemCompraDao.CategoriaCount>> =
        repository.countByCategoria
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /**
     * LiveData for pagination of events (snackbar, navigation, etc.)
     */
    private val _event = MutableLiveData<Event<UiEvent>>()
    val event: LiveData<Event<UiEvent>> = _event

    /**
     * Add a new item to the list
     */
    fun addItem(nome: String, quantidade: String, categoria: String) {
        viewModelScope.launch {
            val item = ItemCompra(
                nome = nome.trim().toTitleCase(),
                quantidade = quantidade.trim(),
                categoria = categoria
            )
            repository.addItem(item)
            _event.value = Event(UiEvent.ItemAdded)
        }
    }

    /**
     * Update an existing item
     */
    fun updateItem(item: ItemCompra, nome: String, quantidade: String, categoria: String) {
        viewModelScope.launch {
            val updatedItem = item.copy(
                nome = nome.trim().toTitleCase(),
                quantidade = quantidade.trim(),
                categoria = categoria
            )
            repository.updateItem(updatedItem)
            _event.value = Event(UiEvent.ItemUpdated)
        }
    }

    /**
     * Toggle the isComprado status of an item
     */
    fun toggleItemStatus(item: ItemCompra) {
        viewModelScope.launch {
            repository.toggleItemStatus(item)
        }
    }

    /**
     * Delete an item from the list
     */
    fun deleteItem(item: ItemCompra) {
        viewModelScope.launch {
            repository.deleteItem(item)
            _event.value = Event(UiEvent.ItemDeleted(item))
        }
    }

    /**
     * Undo a delete operation
     */
    fun undoDelete(deletedItem: ItemCompra) {
        viewModelScope.launch {
            val itemToRestore = deletedItem.copy(id = 0)
            repository.addItem(itemToRestore)
            _event.value = Event(UiEvent.ItemRestored)
        }
    }

    /**
     * Clear the event after it's been handled
     */
    fun onEventHandled() {
        _event.value = null
    }

    /**
     * UI Events sealed class
     */
    sealed class UiEvent {
        object ItemAdded : UiEvent()
        object ItemUpdated : UiEvent()
        data class ItemDeleted(val item: ItemCompra) : UiEvent()
        object ItemRestored : UiEvent()
    }
}

/**
 * Event wrapper for one-time UI actions (like showing Snackbar)
 */
class Event<out T>(private val content: T) {
    var hasBeenHandled = false
        private set

    fun getContent(): T {
        return if (hasBeenHandled) {
            throw IllegalStateException("Event has already been handled.")
        } else {
            hasBeenHandled = true
            content
        }
    }
}

/**
 * ViewModel Factory for manual dependency injection
 */
class ComprasViewModelFactory(private val repository: ComprasRepository) :
    androidx.lifecycle.ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ComprasViewModel::class.java)) {
            return ComprasViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
