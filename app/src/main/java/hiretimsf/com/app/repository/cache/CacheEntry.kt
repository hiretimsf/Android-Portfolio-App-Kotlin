package hiretimsf.com.app.repository.cache

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cache_entries")
data class CacheEntry(
    @PrimaryKey val key: String,
    val payload: String,
    val updatedAtMillis: Long,
)
