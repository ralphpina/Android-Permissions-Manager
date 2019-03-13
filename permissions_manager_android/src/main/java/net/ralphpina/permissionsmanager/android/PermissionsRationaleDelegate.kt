package net.ralphpina.permissionsmanager.android

import androidx.core.app.ActivityCompat
import net.ralphpina.permissionsmanager.Permission
import net.ralphpina.permissionsmanager.android.foregroundutils.ForegroundUtils

/**
 * Delegate to wrap the shouldShowRequestPermissionRationale call to the OS.
 */
internal interface PermissionsRationaleDelegate {
    fun shouldShowRequestPermissionRationale(permission: Permission): Boolean
}

internal class AndroidPermissionsRationaleDelegate(
    private val foregroundUtils: ForegroundUtils
) : PermissionsRationaleDelegate {
    override fun shouldShowRequestPermissionRationale(permission: Permission): Boolean {
        // in the event this gets called without an Activity, we will just return false
        val activity = foregroundUtils.getActivity() ?: return false
        return ActivityCompat.shouldShowRequestPermissionRationale(
            activity,
            permission.value
        )
    }
}