package me.tumur.portfolio.repository.database.dao.profile

import kotlinx.coroutines.flow.Flow
import androidx.room.*
import me.tumur.portfolio.repository.database.model.profile.SocialModel
import me.tumur.portfolio.utils.constants.DbConstants

@Dao
abstract class SocialDao {

    /** Update */
    @Transaction
    open suspend fun update(list: List<SocialModel>) {
        delete()
        insert(list)
    }

    /** Insert */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(list: List<SocialModel>): List<Long>

    /** Delete */
    @Query(DbConstants.SOCIAL_DELETE)
    abstract suspend fun delete()

    /** Get list items */
    @Query(DbConstants.SOCIAL_GET_LIST_ITEMS)
    abstract fun getListItems(id: String): Flow<List<SocialModel>>
}