package net.ralphpina.permissionsmanager

import io.reactivex.Observable
import io.reactivex.Single

/**
 * This is the main API for the SDK. It provides methods to observe and request permissions.
 */
interface PermissionsManager {
    /**
     * Observe the state of permissions. You will get notified when there's new changes to it.
     */
    fun observe(vararg permissions: Permission): Observable<List<PermissionsResult>>

    /**
     * Request a permission from the system. The Single will return the permissions result from the OS.
     */
    fun request(vararg permissions: Permission): Single<List<PermissionsResult>>

    /**
     * Navigate to your app's settings. Here the user can manually change permissions.
     */
    fun navigateToOsAppSettings()
}
