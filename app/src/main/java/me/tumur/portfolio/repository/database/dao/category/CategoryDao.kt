package me.tumur.portfolio.repository.database.dao.category

import androidx.paging.PagingSource
import androidx.room.*
import me.tumur.portfolio.repository.database.model.category.CategoryModel
import me.tumur.portfolio.utils.constants.DbConstants

@Dao
abstract class CategoryDao {

    /** Update */
    @Transaction
    open suspend fun update(list: List<CategoryModel>): List<Long> {
        delete()
        return insert(list)
    }

    /** Insert */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(list: List<CategoryModel>): List<Long>

    /** Delete */
    @Query(DbConstants.CATEGORY_DELETE)
    abstract suspend fun delete()

    /** Get list items */
    @Query(DbConstants.CATEGORY_GET_LIST_ITEMS)
    abstract fun getListItems(type: Int): PagingSource<Int, CategoryModel>
}