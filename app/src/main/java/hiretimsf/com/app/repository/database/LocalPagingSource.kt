package hiretimsf.com.app.repository.database

import androidx.paging.PagingSource
import androidx.paging.PagingState

class LocalPagingSource<T : Any>(
    private val items: () -> List<T>,
) : PagingSource<Int, T>() {

    override fun getRefreshKey(state: PagingState<Int, T>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        return LoadResult.Page(
            data = items(),
            prevKey = null,
            nextKey = null,
        )
    }
}
