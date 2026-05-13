package me.tumur.portfolio.repository.database.dao.settings

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.tumur.portfolio.repository.database.InMemoryDataStore
import me.tumur.portfolio.repository.database.model.settings.AppModel
import javax.inject.Inject

class AppDao @Inject constructor(
    private val store: InMemoryDataStore,
) {
    suspend fun update(list: List<AppModel>) {
        store.app.value = list.sortedBy { it.order }
    }

    suspend fun insert(list: List<AppModel>): List<Long> {
        store.app.value = (store.app.value.filterNot { old -> list.any { it.id == old.id } } + list).sortedBy { it.order }
        return list.indices.map { it.toLong() }
    }

    suspend fun delete() {
        store.app.value = emptyList()
    }

    fun getListItems(): Flow<List<AppModel>> = store.app.map { items ->
        items.sortedBy { it.order }
    }
}
