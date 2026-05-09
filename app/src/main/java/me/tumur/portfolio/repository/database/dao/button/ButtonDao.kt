package me.tumur.portfolio.repository.database.dao.button

import androidx.paging.PagingSource
import androidx.room.*
import me.tumur.portfolio.repository.database.model.button.ButtonModel
import me.tumur.portfolio.utils.constants.DbConstants

@Dao
abstract class ButtonDao {

    /** Update */
    @Transaction
    open suspend fun update(list: List<ButtonModel>): List<Long> {
        delete()
        return insert(list)
    }

    /** Insert */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(list: List<ButtonModel>): List<Long>

    /** Delete */
    @Query(DbConstants.BUTTON_DELETE)
    abstract suspend fun delete()

    /** Get list items */
    @Query(DbConstants.BUTTON_GET_LIST_ITEMS)
    abstract fun getListItems(id: String): PagingSource<Int, ButtonModel>
}