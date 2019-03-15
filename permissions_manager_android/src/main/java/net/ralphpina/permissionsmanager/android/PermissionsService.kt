package net.ralphpina.permissionsmanager.android

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.PermissionChecker
import net.ralphpina.permissionsmanager.Permission
import net.ralphpina.permissionsmanager.Result

/**
 * Service to wrap calls to check permissions.
 */
internal interface PermissionsService {
    val manifestPermissions: List<String>
    fun checkPermission(permission: Permission): Result
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
        PermissionChecker.checkSelfPermission(context, permission.value).mapToResults()
}

internal fun Int.mapToResults() =
    when(this) {
        PermissionChecker.PERMISSION_GRANTED -> Result.GRANTED
        PermissionChecker.PERMISSION_DENIED -> Result.DENIED
        PermissionChecker.PERMISSION_DENIED_APP_OP -> Result.DENIED_APP_OP
        else -> throw IllegalArgumentException("Permissions result passed an unknown value.")
    }
