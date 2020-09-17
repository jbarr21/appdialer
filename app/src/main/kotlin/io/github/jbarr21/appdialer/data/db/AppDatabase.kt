package io.github.jbarr21.appdialer.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [AppEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
  abstract fun appDao(): AppDao
}