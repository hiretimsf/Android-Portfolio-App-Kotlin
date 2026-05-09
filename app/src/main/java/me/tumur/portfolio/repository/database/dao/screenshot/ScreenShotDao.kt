package me.tumur.portfolio.repository.database.dao.screenshot

import kotlinx.coroutines.flow.Flow
import androidx.paging.PagingSource
import androidx.room.*
import me.tumur.portfolio.repository.database.model.screenshot.ScreenShotModel
import me.tumur.portfolio.utils.constants.DbConstants

@Dao
abstract class ScreenShotDao {

    /** Update */
    @Transaction
    open suspend fun update(list: List<ScreenShotModel>): List<Long> {
        delete()
        return insert(list)
    }

    /** Insert */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(list: List<ScreenShotModel>): List<Long>

    /** Delete */
    @Query(DbConstants.SCREENSHOT_DELETE)
    abstract suspend fun delete()

    /** Get paged list items */
    @Query(DbConstants.SCREENSHOT_GET_LIST_ITEMS)
    abstract fun getPagedItems(id: String): PagingSource<Int, ScreenShotModel>

    /** Get list items */
    @Query(DbConstants.SCREENSHOT_GET_LIST_ITEMS)
    abstract fun getListItems(id: String): Flow<List<ScreenShotModel>>
}