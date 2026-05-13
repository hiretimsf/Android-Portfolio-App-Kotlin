package me.tumur.portfolio.repository.database.dao.location

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.tumur.portfolio.repository.database.InMemoryDataStore
import me.tumur.portfolio.repository.database.model.LocationModel
import javax.inject.Inject

class LocationDao @Inject constructor(
    private val store: InMemoryDataStore,
) {
    suspend fun update(list: List<LocationModel>): List<Long> {
        store.locations.value = list
        return list.indices.map { it.toLong() }
    }

    suspend fun insert(list: List<LocationModel>): List<Long> {
        store.locations.value = store.locations.value.filterNot { old -> list.any { it.id == old.id } } + list
        return list.indices.map { it.toLong() }
    }

    suspend fun delete() {
        store.locations.value = emptyList()
    }

    fun getSingleItem(id: String): Flow<LocationModel> = store.locations.map { items ->
        items.first { it.ownerId == id }
    }
}
