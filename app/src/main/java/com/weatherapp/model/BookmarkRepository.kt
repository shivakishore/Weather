package com.weatherapp.model

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.weatherapp.model.valueobject.Bookmark

/**
 * Created by Shiva Kishore on 3/6/2021.
 */
@Database(entities = [Bookmark::class], version = 1)
abstract class BookmarkRepository : RoomDatabase() {
    abstract fun bookmarksDao(): BookmarksDao

    companion object {
        var INSTANCE: BookmarkRepository? = null

        fun getBookmarkRepository(context: Context?): BookmarkRepository? {
            if (INSTANCE == null) {
                synchronized(BookmarkRepository::class) {
                    INSTANCE = Room.databaseBuilder(
                        context!!.applicationContext,
                        BookmarkRepository::class.java,
                        "weatherDB"
                    ).build()
                }
            }
            return INSTANCE
        }

        fun destroyDataBase() {
            INSTANCE = null
        }
    }

    @Dao
    interface BookmarksDao {
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertBookmark(bookmark: Bookmark): Long

        @Update
        suspend fun updateBookmark(bookmark: Bookmark)

        @Delete
        suspend fun deleteBookmark(bookmark: Bookmark)

        @Query("SELECT * FROM bookmarks WHERE name == :name")
        suspend fun getBookmarkByName(name: String): List<Bookmark>

        @Query("SELECT * FROM bookmarks")
        suspend fun getBookmarks(): List<Bookmark>
    }
}

class ABC : BookmarkRepository() {
    override fun bookmarksDao(): BookmarksDao {
        TODO("Not yet implemented")
    }

    override fun createOpenHelper(config: DatabaseConfiguration?): SupportSQLiteOpenHelper {
        TODO("Not yet implemented")
    }

    override fun createInvalidationTracker(): InvalidationTracker {
        TODO("Not yet implemented")
    }

    override fun clearAllTables() {
        TODO("Not yet implemented")
    }

}