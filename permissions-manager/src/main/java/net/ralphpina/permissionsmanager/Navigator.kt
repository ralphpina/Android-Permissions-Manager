package net.ralphpina.permissionsmanager

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS
import net.ralphpina.permissionsmanager.foregroundutils.ForegroundUtils

/**
 * Used to navigate to app settings and to our permissions requesting activity.
 * This is marked internal because we need to use [PermissionRequestActivity]
 * for the SDK to properly work.
 */
internal interface Navigator {
    fun navigateToPermissionRequestActivity(permissions: List<Permission>)
    fun navigateToOsAppSettings()
}

internal class AndroidNavigator(
        private val context: Context,
        private val foregroundUtils: ForegroundUtils
) : Navigator {
    override fun navigateToPermissionRequestActivity(permissions: List<Permission>) {
        val starter = foregroundUtils.getActivity() ?: context
        PermissionRequestActivity.startActivity(
            starter,
            *permissions.map { it.value }.toTypedArray()
        )
    }

    /**
     * Open the app's settings page so the user could switch an activity.
     */
    override fun navigateToOsAppSettings() {
        val starter = foregroundUtils.getActivity() ?: context
        //Open the specific App Info page:
        val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS).also {
            it.data = Uri.parse("package:" + context.packageName)
        }
        if (intent.resolveActivity(starter.packageManager) != null) {
            starter.startActivity(intent)
        } else {
            with (Intent(ACTION_MANAGE_APPLICATIONS_SETTINGS)) {
                if (resolveActivity(starter.packageManager) != null) {
                    starter.startActivity(this)
                }
            }
        }
    }
}
