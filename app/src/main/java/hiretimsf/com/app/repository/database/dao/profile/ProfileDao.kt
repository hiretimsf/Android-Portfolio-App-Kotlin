package hiretimsf.com.app.repository.database.dao.profile

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import hiretimsf.com.app.repository.database.InMemoryDataStore
import hiretimsf.com.app.repository.database.model.profile.ProfileModel
import javax.inject.Inject

class ProfileDao @Inject constructor(
    private val store: InMemoryDataStore,
) {
    suspend fun update(list: List<ProfileModel>) {
        store.profiles.value = list.sortedBy { it.order }
    }

    suspend fun delete() {
        store.profiles.value = emptyList()
    }

    suspend fun insert(list: List<ProfileModel>) {
        store.profiles.value = (store.profiles.value.filterNot { old -> list.any { it.id == old.id } } + list).sortedBy { it.order }
    }

    fun getSingleItem(id: String): Flow<ProfileModel> = store.profiles.map { items ->
        items.first { it.id == id }
    }
}
