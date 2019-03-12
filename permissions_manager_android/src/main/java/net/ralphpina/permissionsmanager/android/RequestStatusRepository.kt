package net.ralphpina.permissionsmanager.android

import android.content.SharedPreferences
import net.ralphpina.permissionsmanager.Permission

internal interface RequestStatusRepository {
    /**
     * Set that a permission has been requested by the app. Regardless or not of
     * whether it was granted.
     */
    fun setHasAsked(permission: Permission)

    /**
     * Have we asked the user for the permission before.
     */
    fun getHasAsked(permission: Permission): Boolean

    /**
     * Clear the data in the database. From app's perspective no permissions have been requested.
     * This is probably not something you'll want to use outside of testing.
     */
    fun clearData()
}

internal class RequestStatusRepositoryImpl(private val preferences: SharedPreferences) :
    RequestStatusRepository {

    override fun setHasAsked(permission: Permission) =
        preferences.edit()
            .putBoolean(permission.value, true)
            .apply()

    override fun getHasAsked(permission: Permission) =
        preferences.getBoolean(permission.value, false)

    override fun clearData() = preferences.edit().clear().apply()
}
