package hiretimsf.com.app.repository.database.dao.category

import androidx.paging.PagingSource
import hiretimsf.com.app.repository.database.InMemoryDataStore
import hiretimsf.com.app.repository.database.LocalPagingSource
import hiretimsf.com.app.repository.database.model.category.CategoryModel
import javax.inject.Inject

class CategoryDao @Inject constructor(
    private val store: InMemoryDataStore,
) {
    suspend fun update(list: List<CategoryModel>): List<Long> {
        store.categories.value = list.sortedBy { it.order }
        return list.indices.map { it.toLong() }
    }

    suspend fun updateForTypes(types: Set<Int>, list: List<CategoryModel>): List<Long> {
        store.categories.value = (
            store.categories.value.filterNot { it.type in types } + list
        ).sortedBy { it.order }
        return list.indices.map { it.toLong() }
    }

    suspend fun insert(list: List<CategoryModel>): List<Long> {
        store.categories.value = (store.categories.value.filterNot { old -> list.any { it.id == old.id } } + list).sortedBy { it.order }
        return list.indices.map { it.toLong() }
    }

    suspend fun delete() {
        store.categories.value = emptyList()
    }

    fun getListItems(type: Int): PagingSource<Int, CategoryModel> = LocalPagingSource {
        store.categories.value.filter { it.type == type }.sortedBy { it.order }
    }
}
