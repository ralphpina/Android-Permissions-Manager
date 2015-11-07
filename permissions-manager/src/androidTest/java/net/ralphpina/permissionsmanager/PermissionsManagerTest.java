package net.ralphpina.permissionsmanager;

import android.support.v4.app.Fragment;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_CALENDAR;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@SmallTest
public class PermissionsManagerTest extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        PermissionsManager.init(getContext());
        PermissionsManager.get()
                          .injectMockSystemPermissions();
    }

    @Override
    protected void tearDown() throws Exception {
        PermissionsManager.tearDown();
        super.tearDown();
    }

    // ===== CAMERA PERMISSIONS ====================================================================

    public void test_camera_permission_not_granted_by_default() throws Exception {
        {
            assertThat(PermissionsManager.get()
                                         .isCameraGranted(), is(false));
        }
    }

    public void test_everAskForCamera_returns_false_if_user_blocked_us() throws Exception {
        {
            assertFalse(PermissionsManager.get()
                                          .neverAskForCamera(new Fragment()));
        }

        PermissionsManager.get()
                          .requestCameraPermission(new Fragment());
        {
            assertFalse(PermissionsManager.get()
                                          .neverAskForCamera(new Fragment()));
        }

        PermissionsManager.get()
                          .requestCameraPermission(new Fragment());
        {
            assertFalse(PermissionsManager.get()
                                          .neverAskForCamera(new Fragment()));
        }

        PermissionsManager.get()
                          .getMockSystemPermissions()
                          .neverAskAgain(CAMERA);
        {
            // now they've really blocked us
            assertTrue(PermissionsManager.get()
                                         .neverAskForCamera(new Fragment()));
        }
    }


    public void test_neverAskForCamera_if_camera_granted() throws Exception {
        PermissionsManager.get()
                          .getMockSystemPermissions()
                          .setIsCameraGranted(true);
        {
            assertFalse(PermissionsManager.get()
                                          .neverAskForCamera(new Fragment()));
        }
    }

    public void test_shouldShowCameraPermissionRationale_camera_granted() throws Exception {
        PermissionsManager.get()
                          .getMockSystemPermissions()
                          .setIsCameraGranted(true);
        {
            assertFalse(PermissionsManager.get()
                                          .shouldShowCameraRationale(new Fragment()));
        }
    }

    public void test_shouldShowCameraPermissionRationale_camera_never_ask_again() throws Exception {
        PermissionsManager.get()
                          .getMockSystemPermissions()
                          .neverAskAgain(CAMERA);
        {
            assertFalse(PermissionsManager.get()
                                          .shouldShowCameraRationale(new Fragment()));
        }
    }

    public void test_persisting_if_we_have_asked_for_camera() throws Exception {
        {
            assertFalse(PermissionsManager.get()
                                          .hasAskedForCameraPermission());
        }

        PermissionsManager.get()
                          .requestCameraPermission(new Fragment());
        {
            assertTrue(PermissionsManager.get()
                                         .hasAskedForCameraPermission());
        }
    }

    // ===== LOCATION PERMISSIONS ==================================================================

    public void test_location_permissions_not_granted_by_default() throws Exception {
        {
            assertFalse(PermissionsManager.get()
                                          .isLocationGranted());
        }
    }

    public void test_neverAskForLocation_returns_true_if_user_blocked_us() throws Exception {
        {
            assertFalse(PermissionsManager.get()
                                          .neverAskForLocation(new Fragment()));
        }

        PermissionsManager.get()
                          .requestLocationPermission(new Fragment());
        {
            assertFalse(PermissionsManager.get()
                                          .neverAskForLocation(new Fragment()));
        }

        PermissionsManager.get()
                          .requestLocationPermission(new Fragment());
        {
            assertFalse(PermissionsManager.get()
                                          .neverAskForLocation(new Fragment()));
        }

        PermissionsManager.get()
                          .getMockSystemPermissions()
                          .neverAskAgain(ACCESS_COARSE_LOCATION);
        {
            assertTrue(PermissionsManager.get()
                                         .neverAskForLocation(new Fragment()));
        }
    }

    public void test_neverAskForLocation_false_with_location_granted() throws Exception {
        PermissionsManager.get()
                          .getMockSystemPermissions()
                          .setIsLocationGranted(true);
        {
            assertFalse(PermissionsManager.get()
                                          .neverAskForLocation(new Fragment()));
        }
    }

    public void test_shouldShowLocationPermissionRationale_no_location() throws Exception {
        PermissionsManager.get()
                          .requestLocationPermission(new Fragment());
        {
            assertTrue(PermissionsManager.get()
                                         .shouldShowLocationRationale(new Fragment()));
        }
    }

    public void test_shouldShowLocationPermissionRationale_with_granted() throws Exception {
        PermissionsManager.get()
                          .getMockSystemPermissions()
                          .setIsStorageGranted(true);
        {
            assertFalse(PermissionsManager.get()
                                          .shouldShowLocationRationale(new Fragment()));
        }
    }

    public void test_shouldShowLocationPermissionRationale_with_never_ask_again() throws Exception {
        PermissionsManager.get()
                          .getMockSystemPermissions()
                          .neverAskAgain(ACCESS_COARSE_LOCATION);
        PermissionsManager.get()
                          .requestLocationPermission(new Fragment());
        {
            assertFalse(PermissionsManager.get()
                                          .shouldShowLocationRationale(new Fragment()));
        }
    }

    // ==== AUDIO RECORDING PERMISSION =============================================================

    public void test_audio_permission_not_granted_by_default() throws Exception {
        {
            assertFalse(PermissionsManager.get()
                                          .isAudioRecordingGranted());
        }
    }

    public void test_neverAskForAudio_returns_false_if_user_blocked_us() throws Exception {
        {
            assertFalse(PermissionsManager.get()
                                          .neverAskForAudio(new Fragment()));
        }

        PermissionsManager.get().requestAudioRecordingPermission(new Fragment());
        {
            assertFalse(PermissionsManager.get().neverAskForAudio(new Fragment()));
        }

        PermissionsManager.get().requestAudioRecordingPermission(new Fragment());
        {
            assertFalse(PermissionsManager.get().neverAskForAudio(new Fragment()));
        }

        PermissionsManager.get().getMockSystemPermissions().neverAskAgain(RECORD_AUDIO);
        {
            assertTrue(PermissionsManager.get()
                                         .neverAskForAudio(new Fragment()));
        }
    }

    public void test_neverAskForAudio_with_recording_granted() throws Exception {
        PermissionsManager.get().getMockSystemPermissions().setIsAudioRecordingGranted(true);
        {
            assertFalse(PermissionsManager.get()
                                          .neverAskForAudio(new Fragment()));
        }
    }

    public void test_shouldShowAudioRecordingRationale_with_audio_granted() throws Exception {
        PermissionsManager.get().getMockSystemPermissions().setIsAudioRecordingGranted(true);
        {
            assertFalse(PermissionsManager.get()
                                          .shouldShowAudioRecordingRationale(new Fragment()));
        }
    }

    public void test_shouldShowAudioRecordingRationale_with_audio_never_ask_again() throws Exception {
        PermissionsManager.get().getMockSystemPermissions()
                           .neverAskAgain(RECORD_AUDIO);
        PermissionsManager.get().requestAudioRecordingPermission(new Fragment());
        {
            assertFalse(PermissionsManager.get().shouldShowAudioRecordingRationale(new Fragment()));
        }
    }

    // ==== CALENDAR ===============================================================================

    public void test_calendar_permission_not_granted_by_default() throws Exception {
        {
            assertFalse(PermissionsManager.get().isCalendarGranted());
        }
    }

    public void test_neverAskForCalendar_returns_false_if_user_blocked_us() throws Exception {
        {
            assertFalse(PermissionsManager.get().neverAskForCalendar(new Fragment()));
        }

        PermissionsManager.get().requestCalendarPermission(new Fragment());
        {
            assertFalse(PermissionsManager.get().neverAskForCalendar(new Fragment()));
        }

        PermissionsManager.get().requestCalendarPermission(new Fragment());
        {
            assertFalse(PermissionsManager.get().neverAskForCalendar(new Fragment()));
        }

        PermissionsManager.get().getMockSystemPermissions().neverAskAgain(READ_CALENDAR);
        {
            assertTrue(PermissionsManager.get().neverAskForCalendar(new Fragment()));
        }
    }

    public void test_neverAskForCalendar_if_granted() throws Exception {
        PermissionsManager.get().getMockSystemPermissions().setIsCalendarGranted(true);
        {
            assertFalse(PermissionsManager.get().neverAskForCalendar(new Fragment()));
        }
    }

    public void test_shouldShowCalendarRationale_not_granted() throws Exception {
        {
            assertFalse(PermissionsManager.get().shouldShowCalendarRationale(new Fragment()));
        }

        PermissionsManager.get().requestCalendarPermission(new Fragment());
        {
            assertTrue(PermissionsManager.get().shouldShowCalendarRationale(new Fragment()));
        }
    }

    public void test_shouldShowCalendarRationale_granted() throws Exception {
        PermissionsManager.get().getMockSystemPermissions().setIsCalendarGranted(true);
        {
            assertFalse(PermissionsManager.get().shouldShowCalendarRationale(new Fragment()));
        }
    }

    public void test_shouldShowCalendarRationale_never_ask_again() throws Exception {
        PermissionsManager.get().getMockSystemPermissions().neverAskAgain(READ_CALENDAR);
        {
            assertFalse(PermissionsManager.get()
                                          .shouldShowCalendarRationale(new Fragment()));
        }
    }

    // ==== CONTACTS ===============================================================================

    public void test_contact_permission_not_granted_by_default() throws Exception {
        {
            assertFalse(PermissionsManager.get().isContactsGranted());
        }
    }

    public void test_neverAskForContacts_returns_false_if_user_blocked_us() throws Exception {
        {
            assertFalse(PermissionsManager.get().neverAskForContacts(new Fragment()));
        }

        PermissionsManager.get().requestContactsPermission(new Fragment());
        {
            assertFalse(PermissionsManager.get()
                                          .neverAskForContacts(new Fragment()));
        }

        PermissionsManager.get().requestContactsPermission(new Fragment());
        {
            assertFalse(PermissionsManager.get()
                                          .neverAskForContacts(new Fragment()));
        }

        PermissionsManager.get().getMockSystemPermissions().neverAskAgain(READ_CONTACTS);
        {
            assertTrue(PermissionsManager.get()
                                         .neverAskForContacts(new Fragment()));
        }
    }

    public void test_neverAskForContacts_if_granted() throws Exception {
        PermissionsManager.get().getMockSystemPermissions().setIsContactsGranted(true);
        {
            assertFalse(PermissionsManager.get().neverAskForContacts(new Fragment()));
        }
    }

    public void test_shouldShowContactsRationale_not_granted() throws Exception {
        {
            assertFalse(PermissionsManager.get().shouldShowContactsRationale(new Fragment()));
        }

        PermissionsManager.get().requestContactsPermission(new Fragment());
        {
            assertTrue(PermissionsManager.get().shouldShowContactsRationale(new Fragment()));
        }
    }

    public void test_shouldShowContactsRationale_granted() throws Exception {
        PermissionsManager.get().getMockSystemPermissions().setIsContactsGranted(true);
        {
            assertFalse(PermissionsManager.get()
                                          .shouldShowContactsRationale(new Fragment()));
        }
    }

    public void test_shouldShowContactsRationale_never_ask_again() throws Exception {
        PermissionsManager.get().getMockSystemPermissions().neverAskAgain(READ_CONTACTS);
        {
            assertFalse(PermissionsManager.get()
                                          .shouldShowContactsRationale(new Fragment()));
        }
    }

    // ==== CALLING ================================================================================

    public void test_calling_permission_not_granted_by_default() throws Exception {
        {
            assertFalse(PermissionsManager.get().isCallingGranted());
        }
    }

    public void test_neverAskForCalling_returns_false_if_user_blocked_us() throws Exception {
        {
            assertFalse(PermissionsManager.get().neverAskForCalling(new Fragment()));
        }

        PermissionsManager.get().requestCallingPermission(new Fragment());
        {
            assertFalse(PermissionsManager.get()
                                          .neverAskForCalling(new Fragment()));
        }

        PermissionsManager.get().requestCallingPermission(new Fragment());
        {
            assertFalse(PermissionsManager.get()
                                          .neverAskForCalling(new Fragment()));
        }

        PermissionsManager.get().getMockSystemPermissions().neverAskAgain(CALL_PHONE);
        {
            assertTrue(PermissionsManager.get()
                                          .neverAskForCalling(new Fragment()));
        }
    }

    public void test_neverAskForCalling_if_granted() throws Exception {
        PermissionsManager.get().getMockSystemPermissions().setIsCallingGranted(true);
        {
            assertFalse(PermissionsManager.get().neverAskForCalling(new Fragment()));
        }
    }

    public void test_shouldShowCallingRationale_not_granted() throws Exception {
        {
            assertFalse(PermissionsManager.get().shouldShowCallingRationale(new Fragment()));
        }

        PermissionsManager.get().requestCallingPermission(new Fragment());
        {
            assertTrue(PermissionsManager.get().shouldShowCallingRationale(new Fragment()));
        }
    }

    public void test_shouldShowCallingRationale_all_granted() throws Exception {
        PermissionsManager.get().getMockSystemPermissions().setIsCallingGranted(true);
        {
            assertFalse(PermissionsManager.get().shouldShowCallingRationale(new Fragment()));
        }
    }

    public void test_shouldShowCallingRationale_never_ask_again() throws Exception {
        PermissionsManager.get().getMockSystemPermissions().neverAskAgain(CALL_PHONE);
        {
            assertFalse(PermissionsManager.get().shouldShowCallingRationale(new Fragment()));
        }
    }

    // ==== STORAGE ================================================================================

    public void test_storage_permission_not_granted_by_default() throws Exception {
        {
            assertFalse(PermissionsManager.get().isStorageGranted());
        }
    }

    public void test_neverAskForStorage_returns_false_if_user_blocked_us() throws Exception {
        {
            assertFalse(PermissionsManager.get().neverAskForStorage(new Fragment()));
        }

        PermissionsManager.get().requestStoragePermission(new Fragment());
        {
            assertFalse(PermissionsManager.get().neverAskForStorage(new Fragment()));
        }

        PermissionsManager.get().requestStoragePermission(new Fragment());
        {
            assertFalse(PermissionsManager.get().neverAskForStorage(new Fragment()));
        }

        PermissionsManager.get().getMockSystemPermissions().neverAskAgain(READ_EXTERNAL_STORAGE);
        {
            assertTrue(PermissionsManager.get().neverAskForStorage(new Fragment()));
        }
    }

    public void test_neverAskForStorage_if_granted() throws Exception {
        PermissionsManager.get().getMockSystemPermissions().setIsStorageGranted(true);
        {
            assertFalse(PermissionsManager.get().neverAskForStorage(new Fragment()));
        }
    }

    public void test_shouldShowRequestStorageRationale_not_granted() throws Exception {
        {
            assertFalse(PermissionsManager.get().shouldShowRequestStorageRationale(new Fragment()));
        }

        PermissionsManager.get().requestStoragePermission(new Fragment());
        {
            assertTrue(PermissionsManager.get().shouldShowRequestStorageRationale(new Fragment()));
        }
    }

    public void test_shouldShowRequestStorageRationale_granted() throws Exception {
        PermissionsManager.get().getMockSystemPermissions().setIsContactsGranted(true);
        {
            assertFalse(PermissionsManager.get().shouldShowContactsRationale(new Fragment()));
        }
    }

    public void test_shouldShowRequestStorageRationale_never_ask_again() throws Exception {
        PermissionsManager.get().getMockSystemPermissions().neverAskAgain(READ_EXTERNAL_STORAGE);
        {
            assertFalse(PermissionsManager.get().shouldShowRequestStorageRationale(new Fragment()));
        }
    }
}