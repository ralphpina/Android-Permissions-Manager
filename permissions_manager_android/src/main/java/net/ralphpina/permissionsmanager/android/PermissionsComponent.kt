package net.ralphpina.permissionsmanager.android

import android.content.Context
import net.ralphpina.permissionsmanager.PermissionsManager
import net.ralphpina.permissionsmanager.android.foregroundutils.ForegroundUtilsComponent

private const val PERMISSIONS_SHARED_PREFS_STORAGE = "permissions_shared_prefs_storage"

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

        /**
         * Required to initialize the [PermissionsManager]. Should be your application context.
         */
        fun context(context: Context): Initializer {
            this.context = context
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
            val sharedPreferences = c.getSharedPreferences(PERMISSIONS_SHARED_PREFS_STORAGE, Context.MODE_PRIVATE)

            _permissionsRepository = PermissionsRepositoryImpl(
                navigator = AndroidNavigator(c, foregroundUtils),
                requestStatusRepository = RequestStatusRepositoryImpl(sharedPreferences),
                permissionsService = AndroidPermissionsService(c),
                permissionsRationaleDelegate = AndroidPermissionsRationaleDelegate(foregroundUtils)
            )

            AppLifecycleObserver.init(checkNotNull(_permissionsRepository))

            return checkNotNull(_permissionsRepository as PermissionsManager)
        }
    }
}
