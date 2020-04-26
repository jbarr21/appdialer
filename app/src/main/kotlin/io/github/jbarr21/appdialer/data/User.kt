package io.github.jbarr21.appdialer.data

import android.os.Process
import android.os.UserHandle
import android.os.UserManager

class UserCache(userManager: UserManager) {
  private val userMap: MutableMap<Int, UserHandle>

  init {
    userMap = userManager
      .userProfiles
      .orEmpty()
      .fold(mutableMapOf()) { map: MutableMap<Int, UserHandle>, user: UserHandle ->
        map.apply { put(user.id, user) }
      }
  }

  fun findById(id: Int) = userMap[id].orMain()
}

val UserHandle.isMain: Boolean
  get() = this == Process.myUserHandle()

val UserHandle.id: Int
  get() = toString().substringAfter('{').substringBefore('}').toInt()

fun UserHandle?.orMain(): UserHandle = this ?: Process.myUserHandle()
