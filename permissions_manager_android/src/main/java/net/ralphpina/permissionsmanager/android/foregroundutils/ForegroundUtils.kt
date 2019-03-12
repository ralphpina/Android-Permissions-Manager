package net.ralphpina.permissionsmanager.android.foregroundutils

import android.app.Activity
import android.app.Application
import android.content.Context

interface ForegroundUtils {
    fun getForegroundedActivity(): Activity?
    fun getActivity(): Activity?
}

internal class ForegroundUtilsImpl(
        private val activityRepository: ActivityRepository
) : ForegroundUtils {

    override fun getForegroundedActivity(): Activity? = activityRepository.foregroundedActivity

    override fun getActivity(): Activity? = activityRepository.foregroundedActivity
            ?: activityRepository.startedActivity ?: activityRepository.createdActivity
}

object ForegroundUtilsComponent {
    class Builder {
        private lateinit var context: Context

        fun context(context: Context): Builder {
            this.context = context
            return this
        }

        fun build(): ForegroundUtils {
            val activityRepository = AndroidActivityRepository()
            (context.applicationContext as Application).registerActivityLifecycleCallbacks(activityRepository)
            return ForegroundUtilsImpl(activityRepository)
        }
    }
}