package net.ralphpina.permissionsmanager.android

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.SingleSubject
import net.ralphpina.permissionsmanager.Permission
import net.ralphpina.permissionsmanager.PermissionResult
import net.ralphpina.permissionsmanager.PermissionsManager

internal interface PermissionsRepository {
    /**
     * Update permissions granted by the OS.
     */
    fun update(results: List<PermissionResult>)

    /**
     * Force refresh the permissions that are being listened to. This would be useful
     * when coming from the background. The user may change permissions outside of the app, and we
     * want to show these updates to potential listeners.
     */
    fun refreshPermissions()
}

internal class PermissionsRepositoryImpl(
    private val navigator: Navigator,
    private val requestStatusRepository: RequestStatusRepository,
    private val permissionsService: PermissionsService,
    private val permissionsRationaleDelegate: PermissionsRationaleDelegate
) : PermissionsRepository,
    PermissionsManager {

    private val observeSubjects = mutableMapOf<List<Permission>, BehaviorSubject<List<PermissionResult>>>()
    // I wanted the response of the request() to come directly from the OS, not the map we may have above.
    // So I have a SingleSubject I return. As opposed to clients having to use something like
    // skip(1) if I returned the BehaviorSubject from observeSubjects.
    private val requestSubjects = mutableMapOf<List<Permission>, SingleSubject<List<PermissionResult>>>()

    override fun observe(vararg permissions: Permission): Observable<List<PermissionResult>> =
        with(permissions.sortedBy { it.value }) {
            validatePermissions()
            if (observeSubjects.contains(this)) {
                checkNotNull(observeSubjects[this]).distinctUntilChanged()
            } else {
                val results = map { it.toResult() }
                val subject = BehaviorSubject.createDefault(results)
                observeSubjects[this] = subject
                subject.distinctUntilChanged()
            }
        }

    override fun request(vararg permissions: Permission): Single<List<PermissionResult>> {
        permissions.validatePermissions()
        permissions.checkListedInManifest()
        permissions.markAllAsAsked()
        // if all permissions are already granted. Let's just return the results.
        return if (permissionsGranted(*permissions)) {
            Single.just(permissions.map { it.toResult() })
        } else {
            with(permissions.sortedBy { it.value }) {
                if (requestSubjects.contains(this)) {
                    checkNotNull(requestSubjects[this])
                } else {
                    val subject = SingleSubject.create<List<PermissionResult>>()
                    requestSubjects[this] = subject
                    navigator.navigateToPermissionRequestActivity(this)
                    subject
                }
            }
        }
    }

    override fun navigateToOsAppSettings() = navigator.navigateToOsAppSettings()

    override fun update(results: List<PermissionResult>) {
        val permissions = results.permissions()
        permissions.validatePermissions()

        // permissions have been updated. lets refresh all our caches
        refreshPermissions()

        // let's notify whomever requested the permissions
        with(permissions.sortedBy { it.value }) {
            requestSubjects.remove(this)?.onSuccess(results.toList())
        }
    }

    override fun refreshPermissions() =
        observeSubjects.keys.forEach { perms ->
            checkNotNull(observeSubjects[perms]).onNext(perms.map { it.toResult() })
        }

    private fun Array<out Permission>.markAllAsAsked() =
        forEach { requestStatusRepository.setHasAsked(it) }

    private fun permissionsGranted(vararg permissions: Permission): Boolean {
        for (i in permissions) {
            if (!permissionsService.checkPermission(i)) {
                return false
            }
        }
        return true
    }

    private fun Permission.toResult(): PermissionResult {
        val isGranted = permissionsService.checkPermission(this)
        return PermissionResult(
            this,
            isGranted,
            hasAskedForPermissions = requestStatusRepository.getHasAsked(this),
            isMarkedAsDontAsk = isMarkedAsDontAsk(isGranted)
        )
    }

    /**
     * If a user ticks the "Don't ask again" box while denying a permission, the system will
     * automatically reject it whenever you select it. There are complex rules that determine the state
     * of this flag. They are documented in [PermissionResult.isMarkedAsDontAsk].
     *
     * If the permission is granted, just set this to false. Which is the default. Otherwise, check
     * each permission in the group:
     * - have we asked for that permission? If so, [PermissionsRationaleDelegate.shouldShowRequestPermissionRationale]
     * should return true. It returns false if we already have the permission, or if the user
     * checked "Don't ask again". So the only reason [RequestStatusRepository.getHasAsked] would return true, and
     * [PermissionsRationaleDelegate.shouldShowRequestPermissionRationale] returns false is when the user clicked
     * "Don't ask again".
     */
    private fun Permission.isMarkedAsDontAsk(isGranted: Boolean) =
        !isGranted && getPermissionsInGroup().any {
            requestStatusRepository.getHasAsked(it) != permissionsRationaleDelegate.shouldShowRequestPermissionRationale(
                it
            )
        }

    private fun Array<out Permission>.checkListedInManifest() =
        forEach { if (!permissionsService.manifestPermissions.contains(it.value)) throw PermissionNotRequestedInManifestException(it) }
}

/**
 * We need to know the permissions in a group to check if the user has checked "Don't ask again" for any of them.
 */
private fun Permission.getPermissionsInGroup(): List<Permission> =
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

private fun List<PermissionResult>.permissions(): List<Permission> = map { it.permission }

private fun List<Permission>.validatePermissions() =
    forEach { if (it.value.trim().isEmpty()) throw InvalidPermissionValueException(it) }

private fun Array<out Permission>.validatePermissions() =
    forEach { if (it.value.trim().isEmpty()) throw InvalidPermissionValueException(it) }

private class InvalidPermissionValueException(permission: Permission) :
    Throwable("No value passed for permission $permission")

private class PermissionNotRequestedInManifestException(it: Permission)
    : Throwable("${it.value} has not been listed in the AndroidManifest file.")
