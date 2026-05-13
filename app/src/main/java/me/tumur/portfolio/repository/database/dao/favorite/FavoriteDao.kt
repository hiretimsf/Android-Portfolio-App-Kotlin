package me.tumur.portfolio.repository.database.dao.favorite

import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.tumur.portfolio.repository.database.InMemoryDataStore
import me.tumur.portfolio.repository.database.LocalPagingSource
import me.tumur.portfolio.repository.database.model.favorite.FavoriteModel
import javax.inject.Inject

class FavoriteDao @Inject constructor(
    private val store: InMemoryDataStore,
) {
    private val activeSources = mutableSetOf<LocalPagingSource<FavoriteModel>>()

    suspend fun update(item: FavoriteModel): Long {
        deleteSingleItem(item.id)
        return insert(item)
    }

    suspend fun insert(item: FavoriteModel): Long {
        store.favorites.value = (store.favorites.value.filterNot { it.id == item.id } + item).sortedBy { it.date }
        invalidateSources()
        return 1L
    }

    suspend fun delete() {
        store.favorites.value = emptyList()
        invalidateSources()
    }

    suspend fun deleteSingleItem(id: String): Int {
        val before = store.favorites.value.size
        store.favorites.value = store.favorites.value.filterNot { it.id == id }
        invalidateSources()
        return before - store.favorites.value.size
    }

    fun getSingleItem(id: String): Flow<FavoriteModel> = store.favorites.map { items ->
        items.first { it.id == id }
    }

    fun getListItems(): PagingSource<Int, FavoriteModel> = LocalPagingSource {
        store.favorites.value.sortedBy { it.date }
    }.also { source ->
        activeSources += source
        source.registerInvalidatedCallback { activeSources -= source }
    }

    fun existSingleItem(id: String): Flow<Int> = store.favorites.map { items ->
        items.count { it.id == id }
    }

    suspend fun getMaxOrder(): FavoriteModel = store.favorites.value.maxBy { it.order }

    fun check(): Flow<Int> = store.favorites.map { it.size }

    private fun invalidateSources() {
        activeSources.toList().forEach { it.invalidate() }
    }
}
