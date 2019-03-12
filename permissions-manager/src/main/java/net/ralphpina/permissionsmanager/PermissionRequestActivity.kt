package net.ralphpina.permissionsmanager

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED

/**
 * The sole purpose of this Activity is to request a permission so we can get the callback via the
 * [onRequestPermissionsResult] callback, which then passes this to our [PermissionsRepository].
 *
 * This activity is transparent, so there's no UI for the user.
 */
@TargetApi(Build.VERSION_CODES.M)
class PermissionRequestActivity : AppCompatActivity() {

    private val permissionsRepository by lazy {
        PermissionsComponent.permissionsRepository
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            handleIntent(intent)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val permissions = intent.getStringArrayExtra(PERMISSIONS_KEY)
        requestPermissions(permissions, PERMISSIONS_REQUEST)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        // If request is cancelled, the result arrays are empty.
        if (requestCode == PERMISSIONS_REQUEST || permissions.isNotEmpty()) {
            val permissionResults = permissions
                    .map { it.toPermission() }
                    .mapIndexed { index, permission ->
                        PermissionsResult(
                                permission,
                                grantResults[index] == PERMISSION_GRANTED,
                                true
                        )
                    }
            permissionsRepository.update(permissionResults)
        }
        finish()
        overridePendingTransition(0, 0)
    }

    companion object {
        private const val PERMISSIONS_KEY = "permissions"
        private const val PERMISSIONS_REQUEST = 420

        @JvmStatic
        internal fun startActivity(context: Context, vararg permissions: String) =
                with(Intent(context, PermissionRequestActivity::class.java)) {
                    putExtra(PERMISSIONS_KEY, permissions)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION
                    context.startActivity(this)
                }
    }
}
