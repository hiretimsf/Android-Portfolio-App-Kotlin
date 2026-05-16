package hiretimsf.com.app.repository.database.dao.screenshot

import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import hiretimsf.com.app.repository.database.InMemoryDataStore
import hiretimsf.com.app.repository.database.LocalPagingSource
import hiretimsf.com.app.repository.database.model.screenshot.ScreenShotModel
import javax.inject.Inject

class ScreenShotDao @Inject constructor(
    private val store: InMemoryDataStore,
) {
    suspend fun update(list: List<ScreenShotModel>): List<Long> {
        store.screenshots.value = list.sortedBy { it.order }
        return list.indices.map { it.toLong() }
    }

    suspend fun updateForOwners(ownerIds: Set<String>, list: List<ScreenShotModel>): List<Long> {
        store.screenshots.value = (
            store.screenshots.value.filterNot { it.ownerId in ownerIds } + list
        ).sortedBy { it.order }
        return list.indices.map { it.toLong() }
    }

    suspend fun insert(list: List<ScreenShotModel>): List<Long> {
        store.screenshots.value = (store.screenshots.value.filterNot { old -> list.any { it.id == old.id } } + list).sortedBy { it.order }
        return list.indices.map { it.toLong() }
    }

    suspend fun delete() {
        store.screenshots.value = emptyList()
    }

    fun getPagedItems(id: String): PagingSource<Int, ScreenShotModel> = LocalPagingSource {
        store.screenshots.value.filter { it.ownerId == id }.sortedBy { it.order }
    }

    fun getListItems(id: String): Flow<List<ScreenShotModel>> = store.screenshots.map { items ->
        items.filter { it.ownerId == id }.sortedBy { it.order }
    }
}
