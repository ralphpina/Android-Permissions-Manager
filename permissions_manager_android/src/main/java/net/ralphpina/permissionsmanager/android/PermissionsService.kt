package net.ralphpina.permissionsmanager.android

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import net.ralphpina.permissionsmanager.Permission

/**
 * Service to wrap calls to check permissions.
 */
internal interface PermissionsService {
    fun checkPermission(permission: Permission): Boolean
}

internal class AndroidPermissionsService(private val context: Context) :
    PermissionsService {
    override fun checkPermission(permission: Permission) =
        ContextCompat.checkSelfPermission(context, permission.value) == PermissionChecker.PERMISSION_GRANTED
}