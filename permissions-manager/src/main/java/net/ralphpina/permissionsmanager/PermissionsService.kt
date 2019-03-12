package net.ralphpina.permissionsmanager

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED

/**
 * Service to wrap calls to check permissions.
 */
interface PermissionsService {
    fun checkPermission(permission: Permission): Boolean
}

internal class AndroidPermissionsService(private val context: Context) : PermissionsService {
    override fun checkPermission(permission: Permission) =
            ContextCompat.checkSelfPermission(context, permission.value) == PERMISSION_GRANTED
}
