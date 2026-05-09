package me.tumur.portfolio.repository.database.dao.task

import androidx.paging.PagingSource
import androidx.room.*
import me.tumur.portfolio.repository.database.model.task.TaskModel
import me.tumur.portfolio.utils.constants.DbConstants

@Dao
abstract class TaskDao {

    /** Update */
    @Transaction
    open suspend fun update(list: List<TaskModel>): List<Long> {
        delete()
        return insert(list)
    }

    /** Insert */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(list: List<TaskModel>): List<Long>

    /** Delete */
    @Query(DbConstants.TASK_DELETE)
    abstract suspend fun delete()

    /** Get list items */
    @Query(DbConstants.TASK_GET_LIST_ITEMS)
    abstract fun getListItems(id: String): PagingSource<Int, TaskModel>
}