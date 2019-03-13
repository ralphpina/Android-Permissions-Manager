package net.ralphpina.permissionsmanager.android

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import net.ralphpina.permissionsmanager.Permission

/**
 * Service to wrap calls to check permissions.
 */
internal interface PermissionsService {
    val manifestPermissions: List<String>
    fun checkPermission(permission: Permission): Boolean
}

internal class AndroidPermissionsService(private val context: Context) :
    PermissionsService {

    override val manifestPermissions: List<String> by lazy {
        context
            .packageManager
            .getPackageInfo(context.packageName, PackageManager.GET_PERMISSIONS)
            .requestedPermissions
            .toList()
    }

    override fun checkPermission(permission: Permission) =
        ContextCompat.checkSelfPermission(context, permission.value) == PermissionChecker.PERMISSION_GRANTED
}
