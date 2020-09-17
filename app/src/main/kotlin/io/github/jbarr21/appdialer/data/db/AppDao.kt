package io.github.jbarr21.appdialer.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AppDao {
  @Query("SELECT * FROM appentity")
  suspend fun getAll(): List<AppEntity>

  @Query("SELECT * FROM appentity WHERE packageName LIKE :packageName AND userId == :userId LIMIT 1")
  suspend fun findByPackageName(packageName: String, userId: Int = 0): AppEntity

  @Insert
  suspend fun insertAll(vararg apps: AppEntity)

  @Query("DELETE FROM appentity")
  suspend fun deleteAll()

  @Delete
  suspend fun delete(app: AppEntity)
}