package io.github.jbarr21.appdialer.data

import io.github.jbarr21.appdialer.data.db.AppEntity

fun App.toAppEntity() = AppEntity(
  userId = user.id,
  packageName = packageName,
  activityName = activityName,
  name = name,
  iconColor = iconColor
)

fun AppEntity.toApp(userCache: UserCache) = App(
  name = name,
  packageName = packageName,
  activityName = activityName,
  user = userCache.findById(userId),
  iconColor = iconColor
)
