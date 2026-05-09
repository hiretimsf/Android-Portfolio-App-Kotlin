package me.tumur.portfolio.repository.database.dao.location

import kotlinx.coroutines.flow.Flow
import androidx.room.*
import me.tumur.portfolio.repository.database.model.LocationModel
import me.tumur.portfolio.utils.constants.DbConstants

@Dao
abstract class LocationDao {

    /** Update */
    @Transaction
    open suspend fun update(list: List<LocationModel>): List<Long> {
        delete()
        return insert(list)
    }

    /** Insert */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(list: List<LocationModel>): List<Long>

    /** Delete */
    @Query(DbConstants.LOCATION_DELETE)
    abstract suspend fun delete()


    /** Get single item */
    @Query(DbConstants.LOCATION_GET_SINGLE_ITEM)
    abstract fun getSingleItem(id: String): Flow<LocationModel>

}