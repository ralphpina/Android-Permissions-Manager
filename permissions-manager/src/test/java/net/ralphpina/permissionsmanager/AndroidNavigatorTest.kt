package net.ralphpina.permissionsmanager

import android.app.Activity
import android.content.Context
import com.nhaarman.mockitokotlin2.*
import net.ralphpina.permissionsmanager.foregroundutils.ForegroundUtils
import kotlin.test.BeforeTest
import kotlin.test.Test

class AndroidNavigatorTest {

    private lateinit var context: Context
    private lateinit var foregroundUtils: ForegroundUtils

    private lateinit var navigator: Navigator

    @BeforeTest
    fun setup() {
        context = mock()
        foregroundUtils = mock()

        navigator = AndroidNavigator(context, foregroundUtils)
    }

    @Test
    fun `GIVEN app has activity WHEN navigating to permissions request THEN use activity`() {
        val activity = mock<Activity>()
        whenever(foregroundUtils.getActivity()).thenReturn(activity)

        verify(activity, never()).startActivity(any())
        verify(context, never()).startActivity(any())

        navigator.navigateToPermissionRequestActivity(listOf(Permission.Camera))

        verify(activity, times(1)).startActivity(any())
        verify(context, never()).startActivity(any())
    }

    @Test
    fun `GIVEN app doesn't have activity WHEN navigating to permissions request THEN use context`() {
        verify(context, never()).startActivity(any())

        navigator.navigateToPermissionRequestActivity(listOf(Permission.Camera))

        verify(context, times(1)).startActivity(any())
    }
}
