package me.tumur.portfolio.repository.database.dao.button

import androidx.paging.PagingSource
import me.tumur.portfolio.repository.database.InMemoryDataStore
import me.tumur.portfolio.repository.database.LocalPagingSource
import me.tumur.portfolio.repository.database.model.button.ButtonModel
import javax.inject.Inject

class ButtonDao @Inject constructor(
    private val store: InMemoryDataStore,
) {
    suspend fun update(list: List<ButtonModel>): List<Long> {
        store.buttons.value = list.sortedBy { it.order }
        return list.indices.map { it.toLong() }
    }

    suspend fun insert(list: List<ButtonModel>): List<Long> {
        store.buttons.value = (store.buttons.value.filterNot { old -> list.any { it.id == old.id } } + list).sortedBy { it.order }
        return list.indices.map { it.toLong() }
    }

    suspend fun delete() {
        store.buttons.value = emptyList()
    }

    fun getListItems(id: String): PagingSource<Int, ButtonModel> = LocalPagingSource {
        store.buttons.value.filter { it.ownerId == id }.sortedBy { it.order }
    }
}
