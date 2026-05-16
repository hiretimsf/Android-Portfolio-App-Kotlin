package hiretimsf.com.app.repository.cache

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CacheEntry::class],
    version = 1,
    exportSchema = false,
)
abstract class PortfolioCacheDatabase : RoomDatabase() {
    abstract fun cacheDao(): CacheDao
}
