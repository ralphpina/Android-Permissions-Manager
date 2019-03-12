package net.ralphpina.permissionsmanager

import android.Manifest
import android.os.Build

/**
 * Maps to permission groups and permissions found here:
 * https://developer.android.com/guide/topics/permissions/overview#permission-groups
 */
sealed class Permission(val value: String) {
    sealed class Calendar(value: String) : Permission(value) {
        object Read : Calendar(Manifest.permission.READ_CALENDAR)
        object Write : Calendar(Manifest.permission.WRITE_CALENDAR)
    }

    sealed class CallLog(value: String) : Permission(value) {
        object Read : CallLog(Manifest.permission.READ_CALL_LOG)
        object Write : CallLog(Manifest.permission.WRITE_CALL_LOG)
        object ProcessOutgoing : CallLog(Manifest.permission.PROCESS_OUTGOING_CALLS)
    }

    // Uses the permission group name, since there's a single permission
    // If new permissions are added, we may want to change this.
    object Camera : Permission(Manifest.permission.CAMERA)

    sealed class Contacts(value: String) : Permission(value) {
        object Read : Contacts(Manifest.permission.READ_CONTACTS)
        object Write : Contacts(Manifest.permission.WRITE_CONTACTS)
        object GetAccounts : Contacts(Manifest.permission.GET_ACCOUNTS)
    }

    sealed class Location(value: String) : Permission(value) {
        object Fine : Location(Manifest.permission.ACCESS_FINE_LOCATION)
        object Coarse : Location(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    // Uses the permission group name, since there's a single permission
    // If new permissions are added, we may want to change this.
    object Microphone : Permission(Manifest.permission.RECORD_AUDIO)

    sealed class Phone(value: String) : Permission(value) {
        object ReadState : Phone(Manifest.permission.READ_PHONE_STATE)
        object ReadNumbers : Phone(readNumbersPermission())
        object Call : Phone(Manifest.permission.CALL_PHONE)
        object Answer : Phone(answerPhoneCallsPermission())
        object AddVoiceMail : Phone(Manifest.permission.ADD_VOICEMAIL)
        object UseSip : Phone(Manifest.permission.USE_SIP)
    }

    // Uses the permission group name, since there's a single permission
    // If new permissions are added, we may want to change this.
    object Sensors : Permission(Manifest.permission.BODY_SENSORS)

    sealed class Sms(value: String) : Permission(value) {
        object Send : Sms(Manifest.permission.SEND_SMS)
        object Receive : Sms(Manifest.permission.RECEIVE_SMS)
        object Read : Sms(Manifest.permission.READ_SMS)
        object ReceiveWapPush : Sms(Manifest.permission.RECEIVE_WAP_PUSH)
        object ReceiveMms : Sms(Manifest.permission.RECEIVE_MMS)
    }

    sealed class Storage(value: String) : Permission(value) {
        object ReadExternal : Storage(Manifest.permission.READ_EXTERNAL_STORAGE)
        object WriteExternal : Storage(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }
}

/**
 * We're targeting below O, so we need to guard before getting this permission.
 */
private fun readNumbersPermission() =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Manifest.permission.READ_PHONE_NUMBERS
    } else ""

/**
 * We're targeting below O, so we need to guard before getting this permission.
 */
private fun answerPhoneCallsPermission() =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Manifest.permission.ANSWER_PHONE_CALLS
    } else ""

/**
 * We need to know the permissions in a group to check if the user has checked "Don't ask again" for any of them.
 */
internal fun Permission.getPermissionsInGroup(): List<Permission> =
    when (this) {
        is Permission.Calendar -> listOf(
            Permission.Calendar.Read,
            Permission.Calendar.Write
        )
        is Permission.CallLog -> listOf(
            Permission.CallLog.Read,
            Permission.CallLog.Write,
            Permission.CallLog.ProcessOutgoing
        )
        is Permission.Camera -> listOf(Permission.Camera)
        is Permission.Contacts -> listOf(
            Permission.Contacts.Read,
            Permission.Contacts.Write,
            Permission.Contacts.GetAccounts
        )
        is Permission.Location -> listOf(
            Permission.Location.Fine,
            Permission.Location.Coarse
        )
        is Permission.Microphone -> listOf(Permission.Microphone)
        is Permission.Phone -> listOf(
            Permission.Phone.ReadState,
            Permission.Phone.ReadNumbers,
            Permission.Phone.Call,
            Permission.Phone.Answer,
            Permission.Phone.AddVoiceMail,
            Permission.Phone.UseSip
        )
        is Permission.Sensors -> listOf(Permission.Sensors)
        is Permission.Sms -> listOf(
            Permission.Sms.Send,
            Permission.Sms.Receive,
            Permission.Sms.Read,
            Permission.Sms.ReceiveWapPush,
            Permission.Sms.ReceiveMms
        )
        is Permission.Storage -> listOf(
            Permission.Storage.ReadExternal,
            Permission.Storage.WriteExternal
        )
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
        readNumbersPermission() -> Permission.Phone.ReadNumbers
        Manifest.permission.CALL_PHONE -> Permission.Phone.Call
        answerPhoneCallsPermission() -> Permission.Phone.Answer
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
