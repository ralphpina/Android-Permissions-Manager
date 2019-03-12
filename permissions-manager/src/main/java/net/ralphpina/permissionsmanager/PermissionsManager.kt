package net.ralphpina.permissionsmanager

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import io.reactivex.Observable
import io.reactivex.Single
import net.ralphpina.permissionsmanager.foregroundutils.ForegroundUtilsComponent
import net.ralphpina.permissionsmanager.foregroundutils.OnAppLifecycleEvent

private const val PERMISSIONS_SHARED_PREFS_STORAGE = "permissions_shared_prefs_storage"

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

/**
 * This component is used to provide dependencies the SDK needs under the hood. It is also
 * used to init it. This class would be used to wire up [PermissionsManager] into Dagger
 * or another DI framework.
 */
object PermissionsComponent {

    private var _permissionsRepository: PermissionsRepository? = null
    internal val permissionsRepository: PermissionsRepository
        get() = checkNotNull(_permissionsRepository) {
            "PermissionsManager has not been built yet. Use PermissionsComponent.Builder."
        }

    /**
     * Initialize the permissions SDK. You can use this to customize the [PermissionsManager] implementation.
     * The only requirement is to pass a [Context].
     */
    class Initializer {
        private var context: Context? = null
        private var requestStatusRepository: RequestStatusRepository? = null
        private var permissionsService: PermissionsService? = null
        private var permissionRationaleDelegate: PermissionRationaleDelegate? = null

        /**
         * Required to initialize the [PermissionsManager]. Should be your application context.
         */
        fun context(context: Context): Initializer {
            this.context = context
            return this
        }

        /**
         * Set a custom [RequestStatusRepository] implementation to keep track of how users ask for
         * permissions. The default implementation uses [SharedPreferences], but you may want some other
         * backing store.
         */
        fun requestStatusRepository(requestStatusRepository: RequestStatusRepository): Initializer {
            this.requestStatusRepository = requestStatusRepository
            return this
        }

        /**
         * Service used to check permissions in the OS.
         */
        fun permissionsService(permissionsService: PermissionsService): Initializer {
            this.permissionsService = permissionsService
            return this
        }

        /**
         * Delegate to check whether we should show the permissions rationale.
         */
        fun permissionRationaleDelegate(permissionRationaleDelegate: PermissionRationaleDelegate): Initializer {
            this.permissionRationaleDelegate = permissionRationaleDelegate
            return this
        }

        /**
         * Should only be called once to get an instance of [PermissionsManager] to inject/use in your app.
         */
        fun prepare(): PermissionsManager {
            check(PermissionsComponent._permissionsRepository == null) {
                "PermissionsManager has been initiated. This build method has been called els ewhere."
            }
            val c = checkNotNull(context) { "You must pass a context to your buider." }
            val foregroundUtils = ForegroundUtilsComponent.Builder().context(c).build()
            val sharedPreferences = c.getSharedPreferences(PERMISSIONS_SHARED_PREFS_STORAGE, MODE_PRIVATE)

            _permissionsRepository = PermissionsRepositoryImpl(
                    navigator = AndroidNavigator(c, foregroundUtils),
                    requestStatusRepository = requestStatusRepository ?: RequestStatusRepositoryImpl(sharedPreferences),
                    permissionsService = permissionsService ?: AndroidPermissionsService(c),
                    permissionRationaleDelegate = permissionRationaleDelegate ?: AndroidPermissionRationaleDelegate(foregroundUtils)
            )

            // we're intentionally leaking this subscription, since it will live for the lifetime of the app.
            foregroundUtils.observe()
                .doOnNext {
                    if (it is OnAppLifecycleEvent.OnAppForegroundedEvent) {
                        checkNotNull(_permissionsRepository).refreshPermissions()
                    }
                }.subscribe()

            return checkNotNull(_permissionsRepository as PermissionsManager)
        }
    }
}
