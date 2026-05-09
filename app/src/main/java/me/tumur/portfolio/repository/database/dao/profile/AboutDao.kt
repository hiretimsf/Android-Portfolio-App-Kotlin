package me.tumur.portfolio.repository.database.dao.profile

import kotlinx.coroutines.flow.Flow
import androidx.room.*
import me.tumur.portfolio.repository.database.model.profile.AboutModel
import me.tumur.portfolio.utils.constants.DbConstants

@Dao
abstract class AboutDao {

    /** Update */
    @Transaction
    open suspend fun update(list: List<AboutModel>) {
        delete()
        insert(list)
    }

    /** Insert */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(list: List<AboutModel>): List<Long>

    /** Delete */
    @Query(DbConstants.ABOUT_DELETE)
    abstract suspend fun delete()

    /** Get list items */
    @Query(DbConstants.ABOUT_GET_LIST_ITEMS)
    abstract fun getListItems(id: String): Flow<List<AboutModel>>
}