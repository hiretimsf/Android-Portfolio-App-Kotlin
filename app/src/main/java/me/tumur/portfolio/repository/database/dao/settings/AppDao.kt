package me.tumur.portfolio.repository.database.dao.settings

import kotlinx.coroutines.flow.Flow
import androidx.room.*
import me.tumur.portfolio.repository.database.model.settings.AppModel
import me.tumur.portfolio.utils.constants.DbConstants

@Dao
abstract class AppDao {

    /** Update */
    @Transaction
    open suspend fun update(list: List<AppModel>) {
        delete()
        insert(list)
    }

    /** Insert */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(list: List<AppModel>): List<Long>

    /** Delete */
    @Query(DbConstants.APP_DELETE)
    abstract suspend fun delete()

    /** Get list items */
    @Query(DbConstants.APP_GET_LIST_ITEMS)
    abstract fun getListItems(): Flow<List<AppModel>>
}