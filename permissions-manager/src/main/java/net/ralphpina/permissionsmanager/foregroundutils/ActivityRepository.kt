package net.ralphpina.permissionsmanager.foregroundutils

import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.lang.ref.WeakReference

internal interface ActivityRepository {
    val foregroundedActivity: Activity?
    val startedActivity: Activity?
    val createdActivity: Activity?
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
    }

    override fun onActivityStopped(activity: Activity) {
        if (_startedActivityRef?.get() == activity) {
            _startedActivityRef = null
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