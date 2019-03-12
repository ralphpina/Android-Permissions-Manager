package net.ralphpina.permissionsmanager

import androidx.core.app.ActivityCompat
import net.ralphpina.permissionsmanager.foregroundutils.ForegroundUtils

/**
 * Delegate to wrap the shouldShowRequestPermissionRationale call to the OS.
 */
interface PermissionRationaleDelegate {
    fun shouldShowRequestPermissionRationale(permission: Permission): Boolean
}

internal class AndroidPermissionRationaleDelegate(
        private val foregroundUtils: ForegroundUtils
) : PermissionRationaleDelegate {
    override fun shouldShowRequestPermissionRationale(permission: Permission): Boolean {
        // in the event this gets called without an Activity, we will just return false
        val activity = foregroundUtils.getActivity() ?: return false
        return ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                permission.value
        )
    }
}
