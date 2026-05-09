package me.tumur.portfolio.repository.database.dao.favorite

import kotlinx.coroutines.flow.Flow
import androidx.paging.PagingSource
import androidx.room.*
import me.tumur.portfolio.repository.database.model.favorite.FavoriteModel
import me.tumur.portfolio.utils.constants.DbConstants

@Dao
abstract class FavoriteDao {

    /** Update single item */
    @Transaction
    open suspend fun update(item: FavoriteModel): Long {
        deleteSingleItem(item.id)
        return insert(item)
    }

    /** Insert */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(item: FavoriteModel): Long

    /** Delete */
    @Query(DbConstants.FAVORITE_DELETE)
    abstract suspend fun delete()

    /** Delete single item */
    @Query(DbConstants.FAVORITE_DELETE_SINGLE_ITEM)
    abstract suspend fun deleteSingleItem(id: String): Int

    /** Get single item */
    @Query(DbConstants.FAVORITE_GET_SINGLE_ITEM)
    abstract fun getSingleItem(id: String): Flow<FavoriteModel>

    /** Get paged list items */
    @Query(DbConstants.FAVORITE_GET_LIST_ITEMS)
    abstract fun getListItems(): PagingSource<Int, FavoriteModel>

    /** Exist single item */
    @Query(DbConstants.FAVORITE_EXIST_SINGLE_ITEM)
    abstract fun existSingleItem(id: String): Flow<Int>

    /** Get max order */
    @Query(DbConstants.FAVORITE_GET_MAX_ORDER)
    abstract suspend fun getMaxOrder(): FavoriteModel

    /** Check table */
    @Query(DbConstants.FAVORITE_CHECK)
    abstract fun check(): Flow<Int>
}