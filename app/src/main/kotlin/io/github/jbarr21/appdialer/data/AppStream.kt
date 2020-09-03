package io.github.jbarr21.appdialer.data

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppStream @Inject constructor() {
  private val appSubject = BehaviorSubject.createDefault<List<App>>(emptyList())

  fun allApps(): Observable<List<App>> {
    return appSubject.hide()
  }

  fun setApps(apps: List<App>) {
    appSubject.onNext(apps)
  }

  fun currentApps() = appSubject.value!!
}
