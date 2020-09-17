package io.github.jbarr21.appdialer.data.db

import android.graphics.Color.TRANSPARENT
import androidx.annotation.ColorInt
import androidx.room.Entity

@Entity(primaryKeys = ["userId", "packageName", "activityName"])
data class AppEntity(
  val userId: Int = 0,
  val packageName: String,
  val activityName: String,
  val name: String,
  @ColorInt val iconColor: Int = TRANSPARENT
)