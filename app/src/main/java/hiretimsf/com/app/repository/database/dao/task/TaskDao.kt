package hiretimsf.com.app.repository.database.dao.task

import androidx.paging.PagingSource
import hiretimsf.com.app.repository.database.InMemoryDataStore
import hiretimsf.com.app.repository.database.LocalPagingSource
import hiretimsf.com.app.repository.database.model.task.TaskModel
import javax.inject.Inject

class TaskDao @Inject constructor(
    private val store: InMemoryDataStore,
) {
    suspend fun update(list: List<TaskModel>): List<Long> {
        store.tasks.value = list.sortedBy { it.order }
        return list.indices.map { it.toLong() }
    }

    suspend fun insert(list: List<TaskModel>): List<Long> {
        store.tasks.value = (store.tasks.value.filterNot { old -> list.any { it.id == old.id } } + list).sortedBy { it.order }
        return list.indices.map { it.toLong() }
    }

    suspend fun delete() {
        store.tasks.value = emptyList()
    }

    fun getListItems(id: String): PagingSource<Int, TaskModel> = LocalPagingSource {
        store.tasks.value.filter { it.ownerId == id }.sortedBy { it.order }
    }
}
