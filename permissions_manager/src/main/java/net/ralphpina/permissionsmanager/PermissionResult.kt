package net.ralphpina.permissionsmanager

/**
 * Result class wrapping the state or a requested permission.
 *
 * @param permission: The [Permission] this class is representing.
 *
 * @param isGranted: whether the permission is granted or not.
 *
 * @param hasAskedForPermissions: whether the user has previously asked for this permission. Keep in mind that
 * [hasAskedForPermissions] may be false, but [isMarkedAsDontAsk] may be true. See docs below for an explanation.
 *
 * @param isMarkedAsDontAsk: If a user ticks the "Don't ask again" box while denying a permission, the system will
 * automatically reject it whenever you try to request it again. In that situation, the only way a user can
 * grant the permission is to navigate to the app settings and then grant the permission.
 *
 * [isMarkedAsDontAsk] is hard to figure out because the OS does not give you an API to know if the user has
 * marked the permission as "Don't ask again".
 *
 * Another complication is that the [isMarkedAsDontAsk] flag could be determined by other permissions in
 * the same group, and these groups can change at any time. For example, currently, there's two permissions
 * in the LOCATION group: ACCESS_COARSE_LOCATION, and ACCESS_FINE_LOCATION. If you request ACCESS_COARSE_LOCATION
 * from the user, and they select "Don't ask again", then the system will automatically reject whenever you ask
 * for ACCESS_FINE_LOCATION. This will happen regardless of whether you have ever asked for ACCESS_FINE_LOCATION
 * in the past.
 *
 * An implementation that sets the [isMarkedAsDontAsk] flag on this class needs to take these
 * rules into account:
 * - is the permission granted? - if so, then `isMarkedAsDontAsk == false`. The system will automatically
 * grant the permission if asked regardless.
 * - If permission is not granted, then we need to look at two interrelated flags: [hasAskedForPermissions] and
 * [ActivityCompat.shouldShowRequestPermissionRationale]. And we need to look at this flag for all permissions
 * in the group of the permission we're looking at. Therefore, in order to know whether the OS will show the
 * permission dialog for ACCESS_FINE_LOCATION we need to check whether they clicked "Don't ask again" on either
 * ACCESS_FINE_LOCATION or ACCESS_COARSE_LOCATION.
 *
 * Take a look at [PermissionsRepositoryImpl] where this is currently being calculated.
 */
data class PermissionResult(
    val permission: Permission,
    val isGranted: Boolean,
    val hasAskedForPermissions: Boolean,
    val isMarkedAsDontAsk: Boolean = false
)
