package hiretimsf.com.app.repository.database.dao.profile

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import hiretimsf.com.app.repository.database.InMemoryDataStore
import hiretimsf.com.app.repository.database.model.profile.SocialModel
import javax.inject.Inject

class SocialDao @Inject constructor(
    private val store: InMemoryDataStore,
) {
    suspend fun update(list: List<SocialModel>) {
        store.socials.value = list.sortedBy { it.order }
    }

    suspend fun insert(list: List<SocialModel>): List<Long> {
        store.socials.value = (store.socials.value.filterNot { old -> list.any { it.id == old.id } } + list).sortedBy { it.order }
        return list.indices.map { it.toLong() }
    }

    suspend fun delete() {
        store.socials.value = emptyList()
    }

    fun getListItems(id: String): Flow<List<SocialModel>> = store.socials.map { items ->
        items.filter { it.ownerId == id }.sortedBy { it.order }
    }
}
