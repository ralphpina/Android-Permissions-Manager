package net.ralphpina.permissionsmanager.android

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.*
import net.ralphpina.permissionsmanager.Permission
import net.ralphpina.permissionsmanager.PermissionsResult
import kotlin.test.BeforeTest
import kotlin.test.Test

class PermissionsRepositoryImplTest {

    private lateinit var navigator: Navigator
    private lateinit var requestStatusRepository: RequestStatusRepository
    private lateinit var permissionsService: PermissionsService
    private lateinit var permissionRationaleDelegate: PermissionRationaleDelegate

    private lateinit var permissionRepository: PermissionsRepositoryImpl

    @BeforeTest
    fun setup() {
        navigator = mock()
        requestStatusRepository = mock()
        permissionsService = mock()
        permissionRationaleDelegate = mock()

        permissionRepository = PermissionsRepositoryImpl(
            navigator,
            requestStatusRepository,
            permissionsService,
            permissionRationaleDelegate
        )
    }

    @Test
    fun `GIVEN no permissions given WHEN observing THEN return results with no permission`() {
        permissionRepository.observe(Permission.Location.Coarse)
            .test()
            .assertNotTerminated()
            .assertValue(
                listOf(
                    PermissionsResult(
                        Permission.Location.Coarse,
                        isGranted = false,
                        hasAskedForPermissions = false
                    )
                )
            )
            .dispose()
    }

    @Test
    fun `GIVEN permission is granted WHEN observing THEN return results with permission`() {
        whenever(permissionsService.checkPermission(Permission.Location.Coarse)).thenReturn(true)

        permissionRepository.observe(Permission.Location.Coarse)
            .test()
            .assertNotTerminated()
            .assertValue(
                listOf(
                    PermissionsResult(
                        Permission.Location.Coarse,
                        isGranted = true,
                        hasAskedForPermissions = false
                    )
                )
            )
            .dispose()
    }

    @Test
    fun `GIVEN some permissions are granted WHEN observing THEN return results with correct permission`() {
        whenever(permissionsService.checkPermission(Permission.Location.Coarse)).thenReturn(true)

        permissionRepository.observe(
            Permission.Location.Coarse,
            Permission.Location.Fine
        )
            .test()
            .assertNotTerminated()
            .assertValue(
                listOf(
                    PermissionsResult(
                        Permission.Location.Coarse,
                        isGranted = true,
                        hasAskedForPermissions = false
                    ),
                    PermissionsResult(
                        Permission.Location.Fine,
                        isGranted = false,
                        hasAskedForPermissions = false
                    )
                )
            )
            .dispose()
    }

    @Test
    fun `GIVEN permissions are granted WHEN requesting THEN return results`() {
        whenever(permissionsService.checkPermission(Permission.Location.Coarse)).thenReturn(true)

        // haven't requested anything
        verify(navigator, never()).navigateToPermissionRequestActivity(any())

        permissionRepository.request(Permission.Location.Coarse)
            .test()
            .assertComplete()
            .assertValue(
                listOf(
                    PermissionsResult(
                        Permission.Location.Coarse,
                        isGranted = true,
                        hasAskedForPermissions = false
                    )
                )
            )
            .dispose()

        // haven't requested anything
        verify(navigator, never()).navigateToPermissionRequestActivity(any())
    }

    @Test
    fun `GIVEN some permissions are granted, others not WHEN requesting THEN request permissions`() {
        whenever(permissionsService.checkPermission(Permission.Location.Coarse)).thenReturn(true)

        // haven't requested anything
        verify(navigator, never()).navigateToPermissionRequestActivity(any())

        permissionRepository.request(
            Permission.Location.Fine,
            Permission.Location.Coarse
        )
            .test()
            .assertNotComplete()
            .assertNoValues()
            .dispose()

        // need to request
        verify(navigator, times(1)).navigateToPermissionRequestActivity(any())
    }

    @Test
    fun `GIVEN permissions WHEN requesting THEN mark them as asked`() {
        verify(requestStatusRepository, never()).setHasAsked(Permission.Location.Coarse)
        verify(requestStatusRepository, never()).setHasAsked(Permission.Location.Fine)

        permissionRepository.request(
            Permission.Location.Fine,
            Permission.Location.Coarse
        )
            .test()
            .assertNotComplete()
            .assertNoValues()
            .dispose()

        verify(requestStatusRepository, times(1)).setHasAsked(Permission.Location.Coarse)
        verify(requestStatusRepository, times(1)).setHasAsked(Permission.Location.Fine)
    }

    @Test
    fun `GIVEN request is in progress WHEN requesting THEN return same subject`() {
        assertThat(
            permissionRepository.request(
                Permission.Location.Fine,
                Permission.Location.Coarse
            )
        )
            .isSameAs(
                permissionRepository.request(
                    Permission.Location.Fine,
                    Permission.Location.Coarse
                )
            )
    }

    @Test
    fun `GIVEN request is in progress WHEN updating results THEN refresh the permissions cache`() {
        whenever(permissionsService.checkPermission(Permission.Location.Coarse)).thenReturn(true)

        val testObserver = permissionRepository.observe(
            Permission.Location.Coarse,
            Permission.Location.Fine
        )
            .test()
            .assertNotTerminated()
            .assertValueCount(1)
            .assertValue(
                listOf(
                    PermissionsResult(
                        Permission.Location.Coarse,
                        isGranted = true,
                        hasAskedForPermissions = false
                    ),
                    PermissionsResult(
                        Permission.Location.Fine,
                        isGranted = false,
                        hasAskedForPermissions = false
                    )
                )
            )

        // let's pretend the system has granted this now
        whenever(permissionsService.checkPermission(Permission.Location.Fine)).thenReturn(true)
        whenever(requestStatusRepository.getHasAsked(Permission.Location.Fine)).thenReturn(true)
        whenever(permissionRationaleDelegate.shouldShowRequestPermissionRationale(Permission.Location.Fine)).thenReturn(
            true
        )

        permissionRepository.update(
            listOf(
                PermissionsResult(
                    Permission.Location.Fine,
                    isGranted = true,
                    hasAskedForPermissions = true
                )
            )
        )

        testObserver
            .assertValueCount(2)
            .assertValueAt(
                1,
                listOf(
                    PermissionsResult(
                        Permission.Location.Coarse,
                        isGranted = true,
                        hasAskedForPermissions = false
                    ),
                    PermissionsResult(
                        Permission.Location.Fine,
                        isGranted = true,
                        hasAskedForPermissions = true
                    )
                )
            )
            .dispose()
    }

    @Test
    fun `GIVEN request is in progress WHEN updating results THEN noop if no one is observing`() {
        whenever(permissionsService.checkPermission(Permission.Location.Coarse)).thenReturn(true)

        permissionRepository.update(
            listOf(
                PermissionsResult(
                    Permission.Location.Fine,
                    isGranted = true,
                    hasAskedForPermissions = true
                )
            )
        )

        permissionRepository.observe(
            Permission.Location.Coarse,
            Permission.Location.Fine
        )
            .test()
            .assertNotTerminated()
            .assertValueCount(1)
            .assertValue(
                listOf(
                    PermissionsResult(
                        Permission.Location.Coarse,
                        isGranted = true,
                        hasAskedForPermissions = false
                    ),
                    PermissionsResult(
                        Permission.Location.Fine,
                        isGranted = false,
                        hasAskedForPermissions = false
                    )
                )
            )
            .dispose()
    }

    @Test
    fun `GIVEN request is in progress WHEN updating results THEN notify requesters`() {
        val testObserver = permissionRepository.request(Permission.Location.Coarse)
            .test()
            .assertNoValues()
            .assertNotComplete()

        whenever(permissionsService.checkPermission(Permission.Location.Coarse)).thenReturn(true)

        permissionRepository.update(
            listOf(
                PermissionsResult(
                    Permission.Location.Coarse,
                    isGranted = true,
                    hasAskedForPermissions = true
                )
            )
        )

        testObserver
            .assertValueCount(1)
            .assertComplete()
            .assertValue(
                listOf(
                    PermissionsResult(
                        Permission.Location.Coarse,
                        isGranted = true,
                        hasAskedForPermissions = true
                    )
                )
            )
            .dispose()
    }

    @Test
    fun `GIVEN requesting permissions WHEN updating results twice THEN notify requesters once`() {
        val testObserver = permissionRepository.request(Permission.Location.Coarse)
            .test()
            .assertNoValues()
            .assertNotComplete()

        whenever(permissionsService.checkPermission(Permission.Location.Coarse)).thenReturn(true)

        permissionRepository.update(
            listOf(
                PermissionsResult(
                    Permission.Location.Coarse,
                    isGranted = true,
                    hasAskedForPermissions = true
                )
            )
        )

        permissionRepository.update(
            listOf(
                PermissionsResult(
                    Permission.Location.Coarse,
                    isGranted = true,
                    hasAskedForPermissions = true
                )
            )
        )

        testObserver
            .assertValueCount(1)
            .assertComplete()
            .assertValue(
                listOf(
                    PermissionsResult(
                        Permission.Location.Coarse,
                        isGranted = true,
                        hasAskedForPermissions = true
                    )
                )
            )
            .dispose()
    }

    @Test
    fun `GIVEN camera permission granted WHEN observing THEN isMarkedAsDontAsk is false`() {
        whenever(permissionsService.checkPermission(Permission.Camera)).thenReturn(true)
        whenever(requestStatusRepository.getHasAsked(Permission.Camera)).thenReturn(true)

        permissionRepository.observe(Permission.Camera)
            .test()
            .assertNotTerminated()
            .assertValueCount(1)
            .assertValue(
                listOf(
                    PermissionsResult(
                        Permission.Camera,
                        isGranted = true,
                        hasAskedForPermissions = true,
                        isMarkedAsDontAsk = false
                    )
                )
            )
            .dispose()
    }

    @Test
    fun `GIVEN camera permission not granted and never asked WHEN observing THEN isMarkedAsDontAsk is false`() {
        permissionRepository.observe(Permission.Camera)
            .test()
            .assertNotTerminated()
            .assertValueCount(1)
            .assertValue(
                listOf(
                    PermissionsResult(
                        Permission.Camera,
                        isGranted = false,
                        hasAskedForPermissions = false,
                        isMarkedAsDontAsk = false
                    )
                )
            )
            .dispose()
    }

    @Test
    fun `GIVEN camera permission not granted and has asked and should show rationale WHEN observing THEN isMarkedAsDontAsk is false`() {
        whenever(requestStatusRepository.getHasAsked(Permission.Camera)).thenReturn(true)
        whenever(permissionRationaleDelegate.shouldShowRequestPermissionRationale(Permission.Camera)).thenReturn(true)

        permissionRepository.observe(Permission.Camera)
            .test()
            .assertNotTerminated()
            .assertValueCount(1)
            .assertValue(
                listOf(
                    PermissionsResult(
                        Permission.Camera,
                        isGranted = false,
                        hasAskedForPermissions = true,
                        isMarkedAsDontAsk = false
                    )
                )
            )
            .dispose()
    }

    @Test
    fun `GIVEN camera permission not granted and has asked and should not show rationale WHEN observing THEN isMarkedAsDontAsk is true`() {
        whenever(requestStatusRepository.getHasAsked(Permission.Camera)).thenReturn(true)
        whenever(permissionRationaleDelegate.shouldShowRequestPermissionRationale(Permission.Camera)).thenReturn(false)

        permissionRepository.observe(Permission.Camera)
            .test()
            .assertNotTerminated()
            .assertValueCount(1)
            .assertValue(
                listOf(
                    PermissionsResult(
                        Permission.Camera,
                        isGranted = false,
                        hasAskedForPermissions = true,
                        isMarkedAsDontAsk = true
                    )
                )
            )
            .dispose()
    }

    @Test
    fun `GIVEN fine location permission marked as don't ask WHEN observing coarse location THEN isMarkedAsDontAsk is true`() {
        whenever(requestStatusRepository.getHasAsked(Permission.Location.Fine)).thenReturn(true)
        whenever(permissionRationaleDelegate.shouldShowRequestPermissionRationale(Permission.Location.Fine)).thenReturn(
            false
        )

        permissionRepository.observe(Permission.Location.Coarse)
            .test()
            .assertNotTerminated()
            .assertValueCount(1)
            .assertValue(
                listOf(
                    PermissionsResult(
                        Permission.Location.Coarse,
                        isGranted = false,
                        hasAskedForPermissions = false,
                        isMarkedAsDontAsk = true
                    )
                )
            )
            .dispose()
    }
}
