package net.ralphpina.permissionsmanager

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner

/**
 * Permissions may have changed while the app was backgrounded. Let's notify our repository to update
 * if needed. For example, the user granting a permissions via settings, then coming back to the app.
 */
internal class AppLifecycleObserver(
    private val permissionsRepository: PermissionsRepository
) : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppDidEnterForeground() = permissionsRepository.refreshPermissions()

    companion object {
        fun init(permissionsRepository: PermissionsRepository) =
            ProcessLifecycleOwner
                .get()
                .lifecycle
                .addObserver(
                    AppLifecycleObserver(checkNotNull(permissionsRepository))
                )
    }
}