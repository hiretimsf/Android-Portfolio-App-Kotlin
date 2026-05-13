package me.tumur.portfolio.repository.database.dao.welcome

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.tumur.portfolio.repository.database.InMemoryDataStore
import me.tumur.portfolio.repository.database.model.welcome.WelcomeModel
import javax.inject.Inject

class WelcomeDao @Inject constructor(
    private val store: InMemoryDataStore,
) {
    suspend fun update(list: List<WelcomeModel>): List<Long> {
        store.welcome.value = list.sortedBy { it.order }
        return list.indices.map { it.toLong() }
    }

    suspend fun check(): Int = store.welcome.value.size

    suspend fun insert(data: List<WelcomeModel>): List<Long> {
        store.welcome.value = (store.welcome.value.filterNot { old -> data.any { it.id == old.id } } + data).sortedBy { it.order }
        return data.indices.map { it.toLong() }
    }

    suspend fun delete() {
        store.welcome.value = emptyList()
    }

    fun getListItems(): Flow<List<WelcomeModel>> = store.welcome.map { items ->
        items.sortedBy { it.order }
    }

    fun getSingleItem(id: String): Flow<WelcomeModel> = store.welcome.map { items ->
        items.first { it.id == id }
    }
}
