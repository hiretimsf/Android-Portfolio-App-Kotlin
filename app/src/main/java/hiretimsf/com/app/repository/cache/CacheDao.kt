package hiretimsf.com.app.repository.cache

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CacheDao {
    @Query("SELECT payload FROM cache_entries WHERE `key` = :key")
    fun observePayload(key: String): Flow<String?>

    @Query("SELECT payload FROM cache_entries WHERE `key` = :key")
    suspend fun getPayload(key: String): String?

    @Upsert
    suspend fun upsert(entry: CacheEntry)
}
