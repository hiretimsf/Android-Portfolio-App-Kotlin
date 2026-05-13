package me.tumur.portfolio.repository.database.dao.experience

import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.tumur.portfolio.repository.database.InMemoryDataStore
import me.tumur.portfolio.repository.database.LocalPagingSource
import me.tumur.portfolio.repository.database.model.experience.ExperienceModel
import javax.inject.Inject

class ExperienceDao @Inject constructor(
    private val store: InMemoryDataStore,
) {
    suspend fun update(list: List<ExperienceModel>): List<Long> {
        store.experiences.value = list.sortedBy { it.order }
        return list.indices.map { it.toLong() }
    }

    suspend fun insert(list: List<ExperienceModel>): List<Long> {
        store.experiences.value = (store.experiences.value.filterNot { old -> list.any { it.id == old.id } } + list).sortedBy { it.order }
        return list.indices.map { it.toLong() }
    }

    suspend fun delete() {
        store.experiences.value = emptyList()
    }

    fun getListItems(id: String): PagingSource<Int, ExperienceModel> = LocalPagingSource {
        store.experiences.value.filter { it.ownerId == id }.sortedBy { it.order }
    }

    fun getSingleItem(id: String): Flow<ExperienceModel> = store.experiences.map { items ->
        items.first { it.id == id }
    }
}
