package net.ralphpina.permissionsmanager

/**
 * Maps to permission groups and permissions found here:
 * https://developer.android.com/guide/topics/permissions/overview#permission-groups
 */
sealed class Permission(val value: String) {
    sealed class Calendar(value: String) : Permission(value) {
        object Read : Calendar("android.permission.READ_CALENDAR")
        object Write : Calendar("android.permission.WRITE_CALENDAR")
    }

    sealed class CallLog(value: String) : Permission(value) {
        object Read : CallLog("android.permission.READ_CALL_LOG")
        object Write : CallLog("android.permission.WRITE_CALL_LOG")
        object ProcessOutgoing : CallLog("android.permission.PROCESS_OUTGOING_CALLS")
    }

    // Uses the permission group name, since there's a single permission
    // If new permissions are added, we may want to change this.
    object Camera : Permission("android.permission.CAMERA")

    sealed class Contacts(value: String) : Permission(value) {
        object Read : Contacts("android.permission.READ_CONTACTS")
        object Write : Contacts("android.permission.WRITE_CONTACTS")
        object GetAccounts : Contacts("android.permission.GET_ACCOUNTS")
    }

    sealed class Location(value: String) : Permission(value) {
        object Fine : Location("android.permission.ACCESS_FINE_LOCATION")
        object Coarse : Location("android.permission.ACCESS_COARSE_LOCATION")
    }

    // Uses the permission group name, since there's a single permission
    // If new permissions are added, we may want to change this.
    object Microphone : Permission("android.permission.RECORD_AUDIO")

    sealed class Phone(value: String) : Permission(value) {
        object ReadState : Phone("android.permission.READ_PHONE_STATE")
        object ReadNumbers : Phone("android.permission.READ_PHONE_NUMBERS")
        object Call : Phone("android.permission.CALL_PHONE")
        object Answer : Phone("android.permission.ANSWER_PHONE_CALLS")
        object AddVoiceMail : Phone("com.android.voicemail.permission.ADD_VOICEMAIL")
        object UseSip : Phone("android.permission.USE_SIP")
    }

    // Uses the permission group name, since there's a single permission
    // If new permissions are added, we may want to change this.
    object Sensors : Permission("android.permission.BODY_SENSORS")

    sealed class Sms(value: String) : Permission(value) {
        object Send : Sms("android.permission.SEND_SMS")
        object Receive : Sms("android.permission.RECEIVE_SMS")
        object Read : Sms("android.permission.READ_SMS")
        object ReceiveWapPush : Sms("android.permission.RECEIVE_WAP_PUSH")
        object ReceiveMms : Sms("android.permission.RECEIVE_MMS")
    }

    sealed class Storage(value: String) : Permission(value) {
        object ReadExternal : Storage("android.permission.READ_EXTERNAL_STORAGE")
        object WriteExternal : Storage("android.permission.WRITE_EXTERNAL_STORAGE")
    }
}
