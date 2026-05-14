package hiretimsf.com.app.repository.database.dao.profile

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import hiretimsf.com.app.repository.database.InMemoryDataStore
import hiretimsf.com.app.repository.database.model.profile.AboutModel
import javax.inject.Inject

class AboutDao @Inject constructor(
    private val store: InMemoryDataStore,
) {
    suspend fun update(list: List<AboutModel>) {
        store.about.value = list.sortedBy { it.order }
    }

    suspend fun insert(list: List<AboutModel>): List<Long> {
        store.about.value = (store.about.value.filterNot { old -> list.any { it.id == old.id } } + list).sortedBy { it.order }
        return list.indices.map { it.toLong() }
    }

    suspend fun delete() {
        store.about.value = emptyList()
    }

    fun getListItems(id: String): Flow<List<AboutModel>> = store.about.map { items ->
        items.filter { it.ownerId == id }.sortedBy { it.order }
    }
}
