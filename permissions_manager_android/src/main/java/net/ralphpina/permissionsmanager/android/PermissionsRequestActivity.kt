package net.ralphpina.permissionsmanager.android

import android.Manifest
import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import net.ralphpina.permissionsmanager.Permission
import net.ralphpina.permissionsmanager.PermissionResult

/**
 * The sole purpose of this Activity is to request a permission so we can get the callback via the
 * [onRequestPermissionsResult] callback, which then passes this to our [PermissionsRepository].
 *
 * This activity is transparent, so there's no UI for the user.
 */
@TargetApi(Build.VERSION_CODES.M)
class PermissionsRequestActivity : AppCompatActivity() {

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
        requestPermissions(permissions,
            PERMISSIONS_REQUEST
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        // If request is cancelled, the result arrays are empty.
        if (requestCode == PERMISSIONS_REQUEST || permissions.isNotEmpty()) {
            val permissionResults = permissions
                    .map { it.toPermission() }
                    .mapIndexed { index, permission ->
                        PermissionResult(
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
                with(Intent(context, PermissionsRequestActivity::class.java)) {
                    putExtra(PERMISSIONS_KEY, permissions)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION
                    context.startActivity(this)
                }
    }
}

/**
 * The OS callback after requesting permissions will pass the permission String, so we want to
 * convert it back into our types.
 */
internal fun String.toPermission() =
    when (this) {
        Manifest.permission.READ_CALENDAR -> Permission.Calendar.Read
        Manifest.permission.WRITE_CALENDAR -> Permission.Calendar.Write
        Manifest.permission.READ_CALL_LOG -> Permission.CallLog.Read
        Manifest.permission.WRITE_CALL_LOG -> Permission.CallLog.Write
        Manifest.permission.PROCESS_OUTGOING_CALLS -> Permission.CallLog.ProcessOutgoing
        Manifest.permission.CAMERA -> Permission.Camera
        Manifest.permission.READ_CONTACTS -> Permission.Contacts.Read
        Manifest.permission.WRITE_CONTACTS -> Permission.Contacts.Write
        Manifest.permission.GET_ACCOUNTS -> Permission.Contacts.GetAccounts
        Manifest.permission.ACCESS_FINE_LOCATION -> Permission.Location.Fine
        Manifest.permission.ACCESS_COARSE_LOCATION -> Permission.Location.Coarse
        Manifest.permission.RECORD_AUDIO -> Permission.Microphone
        Manifest.permission.READ_PHONE_STATE -> Permission.Phone.ReadState
        Manifest.permission.READ_PHONE_NUMBERS -> Permission.Phone.ReadNumbers
        Manifest.permission.CALL_PHONE -> Permission.Phone.Call
        Manifest.permission.ANSWER_PHONE_CALLS -> Permission.Phone.Answer
        Manifest.permission.ADD_VOICEMAIL -> Permission.Phone.AddVoiceMail
        Manifest.permission.USE_SIP -> Permission.Phone.UseSip
        Manifest.permission.BODY_SENSORS -> Permission.Sensors
        Manifest.permission.SEND_SMS -> Permission.Sms.Send
        Manifest.permission.RECEIVE_SMS -> Permission.Sms.Receive
        Manifest.permission.READ_SMS -> Permission.Sms.Read
        Manifest.permission.RECEIVE_WAP_PUSH -> Permission.Sms.ReceiveWapPush
        Manifest.permission.RECEIVE_MMS -> Permission.Sms.ReceiveMms
        Manifest.permission.READ_EXTERNAL_STORAGE -> Permission.Storage.ReadExternal
        Manifest.permission.WRITE_EXTERNAL_STORAGE -> Permission.Storage.WriteExternal
        else -> throw StringToPermissionParseException(this)
    }

class StringToPermissionParseException(value: String) : Throwable("$value is not a known permission")
