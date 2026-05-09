package me.tumur.portfolio.repository.database.dao.welcome

import kotlinx.coroutines.flow.Flow
import androidx.room.*
import me.tumur.portfolio.repository.database.model.welcome.WelcomeModel
import me.tumur.portfolio.utils.constants.DbConstants


@Dao
abstract class WelcomeDao {

    /** Update */
    @Transaction
    open suspend fun update(list: List<WelcomeModel>): List<Long> {
        delete()
        return insert(list)
    }

    /** Check */
    @Query(DbConstants.WELCOME_CHECK)
    abstract suspend fun check(): Int

    /** Insert */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(data: List<WelcomeModel>): List<Long>

    /** Delete */
    @Query(DbConstants.WELCOME_DELETE)
    abstract suspend fun delete()

    /** Get list items */
    @Query(DbConstants.WELCOME_GET_LIST_ITEMS)
    abstract fun getListItems(): Flow<List<WelcomeModel>>

    /** Get single item */
    @Query(DbConstants.WELCOME_GET_SINGLE_ITEM)
    abstract fun getSingleItem(id: String): Flow<WelcomeModel>
}