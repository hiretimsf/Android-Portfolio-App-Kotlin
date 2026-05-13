package me.tumur.portfolio.repository.database.dao.resource

import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.tumur.portfolio.repository.database.InMemoryDataStore
import me.tumur.portfolio.repository.database.LocalPagingSource
import me.tumur.portfolio.repository.database.model.resource.ResourceModel
import javax.inject.Inject

class ResourceDao @Inject constructor(
    private val store: InMemoryDataStore,
) {
    suspend fun update(list: List<ResourceModel>) {
        store.resources.value = list.sortedBy { it.order }
    }

    suspend fun delete() {
        store.resources.value = emptyList()
    }

    suspend fun insert(list: List<ResourceModel>) {
        store.resources.value = (store.resources.value.filterNot { old -> list.any { it.id == old.id } } + list).sortedBy { it.order }
    }

    fun getListItems(id: String): PagingSource<Int, ResourceModel> = LocalPagingSource {
        store.resources.value.filter { it.ownerId == id }.sortedBy { it.order }
    }

    fun check(id: String): Flow<Int> = store.resources.map { items ->
        items.count { it.ownerId == id }
    }
}
