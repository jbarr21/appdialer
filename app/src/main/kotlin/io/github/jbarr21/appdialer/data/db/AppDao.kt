package io.github.jbarr21.appdialer.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AppDao {
  @Query("SELECT * FROM appentity")
  fun getAll(): List<AppEntity>

  @Query("SELECT * FROM appentity WHERE packageName LIKE :packageName AND userId == :userId LIMIT 1")
  fun findByPackageName(packageName: String, userId: Int = 0): AppEntity

  @Insert
  fun insertAll(vararg apps: AppEntity)

  @Query("DELETE FROM appentity")
  fun deleteAll()

  @Delete
  fun delete(app: AppEntity)
}
