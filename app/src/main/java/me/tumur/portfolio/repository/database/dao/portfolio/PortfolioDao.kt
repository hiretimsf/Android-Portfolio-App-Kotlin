package me.tumur.portfolio.repository.database.dao.portfolio

import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.tumur.portfolio.repository.database.InMemoryDataStore
import me.tumur.portfolio.repository.database.LocalPagingSource
import me.tumur.portfolio.repository.database.model.portfolio.PortfolioModel
import javax.inject.Inject

class PortfolioDao @Inject constructor(
    private val store: InMemoryDataStore,
) {
    suspend fun update(list: List<PortfolioModel>): List<Long> {
        store.portfolio.value = list.sortedBy { it.order }
        return list.indices.map { it.toLong() }
    }

    suspend fun insert(list: List<PortfolioModel>): List<Long> {
        store.portfolio.value = (store.portfolio.value.filterNot { old -> list.any { it.id == old.id } } + list).sortedBy { it.order }
        return list.indices.map { it.toLong() }
    }

    suspend fun delete() {
        store.portfolio.value = emptyList()
    }

    fun getListItems(id: String): PagingSource<Int, PortfolioModel> = LocalPagingSource {
        store.portfolio.value.filter { it.ownerId == id }.sortedBy { it.order }
    }

    fun getSingleItem(id: String): Flow<PortfolioModel> = store.portfolio.map { items ->
        items.first { it.id == id }
    }
}
