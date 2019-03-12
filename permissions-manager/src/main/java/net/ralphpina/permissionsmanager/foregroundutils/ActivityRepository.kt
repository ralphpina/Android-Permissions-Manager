package net.ralphpina.permissionsmanager.foregroundutils

import android.app.Activity
import android.app.Application
import android.os.Bundle
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.lang.ref.WeakReference

internal interface ActivityRepository {
    val foregroundedActivity: Activity?
    val startedActivity: Activity?
    val createdActivity: Activity?
    fun observe(): Observable<OnAppLifecycleEvent>
}

internal class AndroidActivityRepository : ActivityRepository, Application.ActivityLifecycleCallbacks {

    private var _foregroundActivityRef: WeakReference<Activity>? = null
    override val foregroundedActivity: Activity?
        get() = _foregroundActivityRef?.get()

    private var _startedActivityRef: WeakReference<Activity>? = null
    override val startedActivity: Activity?
        get() = _startedActivityRef?.get()

    private var _createdActivityRef: WeakReference<Activity>? = null
    override val createdActivity: Activity?
        get() = _createdActivityRef?.get()

    private val subject = PublishSubject.create<OnAppLifecycleEvent>()
    private var startedActivitiesCount = 0

    override fun observe(): Observable<OnAppLifecycleEvent> = subject

    override fun onActivityResumed(activity: Activity) {
        _foregroundActivityRef = WeakReference(activity)
    }

    override fun onActivityPaused(activity: Activity) {
        if (_foregroundActivityRef?.get() == activity) {
            _foregroundActivityRef = null
        }
    }

    override fun onActivityStarted(activity: Activity) {
        _startedActivityRef = WeakReference(activity)
        startedActivitiesCount += 1
        if (startedActivitiesCount == 1) {
            subject.onNext(OnAppLifecycleEvent.OnAppForegroundedEvent)
        }
    }

    override fun onActivityStopped(activity: Activity) {
        if (_startedActivityRef?.get() == activity) {
            _startedActivityRef = null
        }

        startedActivitiesCount -= 1
        if (startedActivitiesCount == 0) {
            subject.onNext(OnAppLifecycleEvent.OnAppBackgroundedEvent)
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        _createdActivityRef = WeakReference(activity)
    }

    override fun onActivityDestroyed(activity: Activity) {
        if (_createdActivityRef?.get() == activity) {
            _createdActivityRef = null
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
    }
}