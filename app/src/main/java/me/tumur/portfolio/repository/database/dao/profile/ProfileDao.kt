package me.tumur.portfolio.repository.database.dao.profile

import kotlinx.coroutines.flow.Flow
import androidx.room.*
import me.tumur.portfolio.repository.database.model.profile.ProfileModel
import me.tumur.portfolio.utils.constants.DbConstants

@Dao
abstract class ProfileDao {

    /** Update */
    @Transaction
    open suspend fun update(list: List<ProfileModel>) {
        delete()
        insert(list)
    }

    /** Delete */
    @Query(DbConstants.PROFILE_DELETE)
    abstract suspend fun delete()

    /** Insert */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(list: List<ProfileModel>)

    /** Get single item */
    @Query(DbConstants.PROFILE_GET_SINGLE_ITEM)
    abstract fun getSingleItem(id: String): Flow<ProfileModel>


}