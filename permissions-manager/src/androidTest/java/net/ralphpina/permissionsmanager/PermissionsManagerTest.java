package net.ralphpina.permissionsmanager;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.BODY_SENSORS;
import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_CALENDAR;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.SEND_SMS;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class PermissionsManagerTest {

    @Before
    public void setUp() {
        PermissionsManager.init(InstrumentationRegistry.getTargetContext());
        PermissionsManager.get()
                .injectMockSystemPermissions();
        PermissionsManager.get()
                .injectImmediateScheduler();
    }

    @After
    public void tearDown() {
        PermissionsManager.tearDown();
    }

    // ===== CAMERA PERMISSIONS ====================================================================

    @Test
    public void test_camera_permission_not_granted_by_default() throws Exception {
        {
            assertFalse(PermissionsManager.get()
                                .isCameraGranted());
        }
    }

    @Test
    public void test_everAskForCamera_returns_false_if_user_blocked_us() throws Exception {
        {
            assertFalse(PermissionsManager.get()
                                .neverAskForCamera(new Fragment()));
        }

        PermissionsManager.get()
                .requestCameraPermission()
                .subscribe();
        {
            assertFalse(PermissionsManager.get()
                                .neverAskForCamera(new Fragment()));
        }

        PermissionsManager.get()
                .requestCameraPermission()
                .subscribe();
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

    @Test
    public void test_neverAskForCamera_if_camera_granted() throws Exception {
        PermissionsManager.get()
                .getMockSystemPermissions()
                .setIsCameraGranted(true);
        {
            assertFalse(PermissionsManager.get()
                                .neverAskForCamera(new Fragment()));
        }
    }

    @Test
    public void test_shouldShowCameraPermissionRationale_camera_granted() throws Exception {
        PermissionsManager.get()
                .getMockSystemPermissions()
                .setIsCameraGranted(true);
        {
            assertFalse(PermissionsManager.get()
                                .shouldShowCameraRationale(new Fragment()));
        }
    }

    @Test
    public void test_shouldShowCameraPermissionRationale_camera_never_ask_again() throws Exception {
        PermissionsManager.get()
                .getMockSystemPermissions()
                .neverAskAgain(CAMERA);
        {
            assertFalse(PermissionsManager.get()
                                .shouldShowCameraRationale(new Fragment()));
        }
    }

    @Test
    public void test_persisting_if_we_have_asked_for_camera() throws Exception {
        {
            assertFalse(PermissionsManager.get()
                                .hasAskedForCameraPermission());
        }

        PermissionsManager.get()
                .requestCameraPermission();
        {
            assertTrue(PermissionsManager.get()
                               .hasAskedForCameraPermission());
        }
    }

    // ===== LOCATION PERMISSIONS ==================================================================

    @Test
    public void test_location_permissions_not_granted_by_default() throws Exception {
        {
            assertFalse(PermissionsManager.get()
                                .isLocationGranted());
        }
    }

    @Test
    public void test_neverAskForLocation_returns_true_if_user_blocked_us() throws Exception {
        {
            assertFalse(PermissionsManager.get()
                                .neverAskForLocation(new Fragment()));
        }

        PermissionsManager.get()
                .requestLocationPermission()
                .subscribe();
        {
            assertFalse(PermissionsManager.get()
                                .neverAskForLocation(new Fragment()));
        }

        PermissionsManager.get()
                .requestLocationPermission()
                .subscribe();
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

    @Test
    public void test_neverAskForLocation_false_with_location_granted() throws Exception {
        PermissionsManager.get()
                .getMockSystemPermissions()
                .setIsLocationGranted(true);
        {
            assertFalse(PermissionsManager.get()
                                .neverAskForLocation(new Fragment()));
        }
    }

    @Test
    public void test_shouldShowLocationPermissionRationale_no_location() throws Exception {
        PermissionsManager.get()
                .requestLocationPermission()
                .subscribe();
        {
            assertTrue(PermissionsManager.get()
                               .shouldShowLocationRationale(new Fragment()));
        }
    }

    @Test
    public void test_shouldShowLocationPermissionRationale_with_granted() throws Exception {
        PermissionsManager.get()
                .getMockSystemPermissions()
                .setIsStorageGranted(true);
        {
            assertFalse(PermissionsManager.get()
                                .shouldShowLocationRationale(new Fragment()));
        }
    }

    @Test
    public void test_shouldShowLocationPermissionRationale_with_never_ask_again() throws Exception {
        PermissionsManager.get()
                .getMockSystemPermissions()
                .neverAskAgain(ACCESS_COARSE_LOCATION);
        PermissionsManager.get()
                .requestLocationPermission()
                .subscribe();
        {
            assertFalse(PermissionsManager.get()
                                .shouldShowLocationRationale(new Fragment()));
        }
    }

    // ==== AUDIO RECORDING PERMISSION =============================================================

    @Test
    public void test_audio_permission_not_granted_by_default() throws Exception {
        {
            assertFalse(PermissionsManager.get()
                                .isMicrophoneGranted());
        }
    }

    @Test
    public void test_neverAskForAudio_returns_false_if_user_blocked_us() throws Exception {
        {
            assertFalse(PermissionsManager.get()
                                .neverAskForMicrophone(new Fragment()));
        }

        PermissionsManager.get()
                .requestMicrophonePermission()
                .subscribe();
        {
            assertFalse(PermissionsManager.get()
                                .neverAskForMicrophone(new Fragment()));
        }

        PermissionsManager.get()
                .requestMicrophonePermission()
                .subscribe();
        {
            assertFalse(PermissionsManager.get()
                                .neverAskForMicrophone(new Fragment()));
        }

        PermissionsManager.get()
                .getMockSystemPermissions()
                .neverAskAgain(RECORD_AUDIO);
        {
            assertTrue(PermissionsManager.get()
                               .neverAskForMicrophone(new Fragment()));
        }
    }

    @Test
    public void test_neverAskForAudio_with_recording_granted() throws Exception {
        PermissionsManager.get()
                .getMockSystemPermissions()
                .setIsAudioRecordingGranted(true);
        {
            assertFalse(PermissionsManager.get()
                                .neverAskForMicrophone(new Fragment()));
        }
    }

    @Test
    public void test_shouldShowAudioRecordingRationale_with_audio_granted() throws Exception {
        PermissionsManager.get()
                .getMockSystemPermissions()
                .setIsAudioRecordingGranted(true);
        {
            assertFalse(PermissionsManager.get()
                                .shouldShowMicrophoneRationale(new Fragment()));
        }
    }

    @Test
    public void test_shouldShowAudioRecordingRationale_with_audio_never_ask_again() throws Exception {
        PermissionsManager.get()
                .getMockSystemPermissions()
                .neverAskAgain(RECORD_AUDIO);
        PermissionsManager.get()
                .requestMicrophonePermission()
                .subscribe();
        {
            assertFalse(PermissionsManager.get()
                                .shouldShowMicrophoneRationale(new Fragment()));
        }
    }

    // ==== CALENDAR ===============================================================================

    @Test
    public void test_calendar_permission_not_granted_by_default() throws Exception {
        {
            assertFalse(PermissionsManager.get()
                                .isCalendarGranted());
        }
    }

    @Test
    public void test_neverAskForCalendar_returns_false_if_user_blocked_us() throws Exception {
        {
            assertFalse(PermissionsManager.get()
                                .neverAskForCalendar(new Fragment()));
        }

        PermissionsManager.get()
                .requestCalendarPermission()
                .subscribe();
        {
            assertFalse(PermissionsManager.get()
                                .neverAskForCalendar(new Fragment()));
        }

        PermissionsManager.get()
                .requestCalendarPermission()
                .subscribe();
        {
            assertFalse(PermissionsManager.get()
                                .neverAskForCalendar(new Fragment()));
        }

        PermissionsManager.get()
                .getMockSystemPermissions()
                .neverAskAgain(READ_CALENDAR);
        {
            assertTrue(PermissionsManager.get()
                               .neverAskForCalendar(new Fragment()));
        }
    }

    @Test
    public void test_neverAskForCalendar_if_granted() throws Exception {
        PermissionsManager.get()
                .getMockSystemPermissions()
                .setIsCalendarGranted(true);
        {
            assertFalse(PermissionsManager.get()
                                .neverAskForCalendar(new Fragment()));
        }
    }

    @Test
    public void test_shouldShowCalendarRationale_not_granted() throws Exception {
        {
            assertFalse(PermissionsManager.get()
                                .shouldShowCalendarRationale(new Fragment()));
        }

        PermissionsManager.get()
                .requestCalendarPermission()
                .subscribe();
        {
            assertTrue(PermissionsManager.get()
                               .shouldShowCalendarRationale(new Fragment()));
        }
    }

    @Test
    public void test_shouldShowCalendarRationale_granted() throws Exception {
        PermissionsManager.get()
                .getMockSystemPermissions()
                .setIsCalendarGranted(true);
        {
            assertFalse(PermissionsManager.get()
                                .shouldShowCalendarRationale(new Fragment()));
        }
    }

    @Test
    public void test_shouldShowCalendarRationale_never_ask_again() throws Exception {
        PermissionsManager.get()
                .getMockSystemPermissions()
                .neverAskAgain(READ_CALENDAR);
        {
            assertFalse(PermissionsManager.get()
                                .shouldShowCalendarRationale(new Fragment()));
        }
    }

    // ==== CONTACTS ===============================================================================

    @Test
    public void test_contact_permission_not_granted_by_default() throws Exception {
        {
            assertFalse(PermissionsManager.get()
                                .isContactsGranted());
        }
    }

    @Test
    public void test_neverAskForContacts_returns_false_if_user_blocked_us() throws Exception {
        {
            assertFalse(PermissionsManager.get()
                                .neverAskForContacts(new Fragment()));
        }

        PermissionsManager.get()
                .requestContactsPermission()
                .subscribe();
        {
            assertFalse(PermissionsManager.get()
                                .neverAskForContacts(new Fragment()));
        }

        PermissionsManager.get()
                .requestContactsPermission()
                .subscribe();
        {
            assertFalse(PermissionsManager.get()
                                .neverAskForContacts(new Fragment()));
        }

        PermissionsManager.get()
                .getMockSystemPermissions()
                .neverAskAgain(READ_CONTACTS);
        {
            assertTrue(PermissionsManager.get()
                               .neverAskForContacts(new Fragment()));
        }
    }

    @Test
    public void test_neverAskForContacts_if_granted() throws Exception {
        PermissionsManager.get()
                .getMockSystemPermissions()
                .setIsContactsGranted(true);
        {
            assertFalse(PermissionsManager.get()
                                .neverAskForContacts(new Fragment()));
        }
    }

    @Test
    public void test_shouldShowContactsRationale_not_granted() throws Exception {
        {
            assertFalse(PermissionsManager.get()
                                .shouldShowContactsRationale(new Fragment()));
        }

        PermissionsManager.get()
                .requestContactsPermission()
                .subscribe();
        {
            assertTrue(PermissionsManager.get()
                               .shouldShowContactsRationale(new Fragment()));
        }
    }

    @Test
    public void test_shouldShowContactsRationale_granted() throws Exception {
        PermissionsManager.get()
                .getMockSystemPermissions()
                .setIsContactsGranted(true);
        {
            assertFalse(PermissionsManager.get()
                                .shouldShowContactsRationale(new Fragment()));
        }
    }

    @Test
    public void test_shouldShowContactsRationale_never_ask_again() throws Exception {
        PermissionsManager.get()
                .getMockSystemPermissions()
                .neverAskAgain(READ_CONTACTS);
        {
            assertFalse(PermissionsManager.get()
                                .shouldShowContactsRationale(new Fragment()));
        }
    }

    // ==== CALLING ================================================================================

    @Test
    public void test_calling_permission_not_granted_by_default() throws Exception {
        {
            assertFalse(PermissionsManager.get()
                                .isPhoneGranted());
        }
    }

    @Test
    public void test_neverAskForCalling_returns_false_if_user_blocked_us() throws Exception {
        {
            assertFalse(PermissionsManager.get()
                                .neverAskForPhone(new Fragment()));
        }

        PermissionsManager.get()
                .requestPhonePermission()
                .subscribe();
        {
            assertFalse(PermissionsManager.get()
                                .neverAskForPhone(new Fragment()));
        }

        PermissionsManager.get()
                .requestPhonePermission()
                .subscribe();
        {
            assertFalse(PermissionsManager.get()
                                .neverAskForPhone(new Fragment()));
        }

        PermissionsManager.get()
                .getMockSystemPermissions()
                .neverAskAgain(CALL_PHONE);
        {
            assertTrue(PermissionsManager.get()
                               .neverAskForPhone(new Fragment()));
        }
    }

    @Test
    public void test_neverAskForCalling_if_granted() throws Exception {
        PermissionsManager.get()
                .getMockSystemPermissions()
                .setIsCallingGranted(true);
        {
            assertFalse(PermissionsManager.get()
                                .neverAskForPhone(new Fragment()));
        }
    }

    @Test
    public void test_shouldShowCallingRationale_not_granted() throws Exception {
        {
            assertFalse(PermissionsManager.get()
                                .shouldShowRequestPhoneRationale(new Fragment()));
        }

        PermissionsManager.get()
                .requestPhonePermission()
                .subscribe();
        {
            assertTrue(PermissionsManager.get()
                               .shouldShowRequestPhoneRationale(new Fragment()));
        }
    }

    @Test
    public void test_shouldShowCallingRationale_all_granted() throws Exception {
        PermissionsManager.get()
                .getMockSystemPermissions()
                .setIsCallingGranted(true);
        {
            assertFalse(PermissionsManager.get()
                                .shouldShowRequestPhoneRationale(new Fragment()));
        }
    }

    @Test
    public void test_shouldShowCallingRationale_never_ask_again() throws Exception {
        PermissionsManager.get()
                .getMockSystemPermissions()
                .neverAskAgain(CALL_PHONE);
        {
            assertFalse(PermissionsManager.get()
                                .shouldShowRequestPhoneRationale(new Fragment()));
        }
    }

    // ==== STORAGE ================================================================================

    @Test
    public void test_storage_permission_not_granted_by_default() throws Exception {
        {
            assertFalse(PermissionsManager.get()
                                .isStorageGranted());
        }
    }

    @Test
    public void test_neverAskForStorage_returns_false_if_user_blocked_us() throws Exception {
        {
            assertFalse(PermissionsManager.get()
                                .neverAskForStorage(new Fragment()));
        }

        PermissionsManager.get()
                .requestStoragePermission()
                .subscribe();
        {
            assertFalse(PermissionsManager.get()
                                .neverAskForStorage(new Fragment()));
        }

        PermissionsManager.get()
                .requestStoragePermission()
                .subscribe();
        {
            assertFalse(PermissionsManager.get()
                                .neverAskForStorage(new Fragment()));
        }

        PermissionsManager.get()
                .getMockSystemPermissions()
                .neverAskAgain(READ_EXTERNAL_STORAGE);
        {
            assertTrue(PermissionsManager.get()
                               .neverAskForStorage(new Fragment()));
        }
    }

    @Test
    public void test_neverAskForStorage_if_granted() throws Exception {
        PermissionsManager.get()
                .getMockSystemPermissions()
                .setIsStorageGranted(true);
        {
            assertFalse(PermissionsManager.get()
                                .neverAskForStorage(new Fragment()));
        }
    }

    @Test
    public void test_shouldShowRequestStorageRationale_not_granted() throws Exception {
        {
            assertFalse(PermissionsManager.get()
                                .shouldShowRequestStorageRationale(new Fragment()));
        }

        PermissionsManager.get()
                .requestStoragePermission()
                .subscribe();
        {
            assertTrue(PermissionsManager.get()
                               .shouldShowRequestStorageRationale(new Fragment()));
        }
    }

    @Test
    public void test_shouldShowRequestStorageRationale_granted() throws Exception {
        PermissionsManager.get()
                .getMockSystemPermissions()
                .setIsStorageGranted(true);
        {
            assertFalse(PermissionsManager.get()
                                .shouldShowRequestStorageRationale(new Fragment()));
        }
    }

    @Test
    public void test_shouldShowRequestStorageRationale_never_ask_again() throws Exception {
        PermissionsManager.get()
                .getMockSystemPermissions()
                .neverAskAgain(READ_EXTERNAL_STORAGE);
        {
            assertFalse(PermissionsManager.get()
                                .shouldShowRequestStorageRationale(new Fragment()));
        }
    }

    // ==== BODY SENSOR ============================================================================

    @SuppressWarnings("NewApi")
    @Test
    public void test_body_sensor_permission_not_granted_by_default() throws Exception {
        {
            assertFalse(PermissionsManager.get()
                                .isBodySensorGranted());
        }
    }

    @SuppressWarnings("NewApi")
    @Test
    public void test_neverAskForBodySensor_returns_false_if_user_blocked_us() throws Exception {
        {
            assertFalse(PermissionsManager.get()
                                .neverAskForBodySensor(new Fragment()));
        }

        PermissionsManager.get()
                .requestBodySensorPermission()
                .subscribe();
        {
            assertFalse(PermissionsManager.get()
                                .neverAskForBodySensor(new Fragment()));
        }

        PermissionsManager.get()
                .requestBodySensorPermission()
                .subscribe();
        {
            assertFalse(PermissionsManager.get()
                                .neverAskForBodySensor(new Fragment()));
        }

        PermissionsManager.get()
                .getMockSystemPermissions()
                .neverAskAgain(BODY_SENSORS);
        {
            assertTrue(PermissionsManager.get()
                               .neverAskForBodySensor(new Fragment()));
        }
    }

    @SuppressWarnings("NewApi")
    @Test
    public void test_neverAskForBodySensor_if_granted() throws Exception {
        PermissionsManager.get()
                .getMockSystemPermissions()
                .setIsBodySensorsGranted(true);
        {
            assertFalse(PermissionsManager.get()
                                .neverAskForBodySensor(new Fragment()));
        }
    }

    @SuppressWarnings("NewApi")
    @Test
    public void test_shouldShowRequestBodySensorRationale_not_granted() throws Exception {
        {
            assertFalse(PermissionsManager.get()
                                .shouldShowBodySensorRationale(new Fragment()));
        }

        PermissionsManager.get()
                .requestBodySensorPermission()
                .subscribe();

        assertTrue(PermissionsManager.get()
                           .shouldShowBodySensorRationale(new Fragment()));
    }

    @SuppressWarnings("NewApi")
    @Test
    public void test_shouldShowRequestBodySensorRationale_granted() throws Exception {
        PermissionsManager.get()
                .getMockSystemPermissions()
                .setIsBodySensorsGranted(true);
        {
            assertFalse(PermissionsManager.get()
                                .shouldShowBodySensorRationale(new Fragment()));
        }
    }

    @SuppressWarnings("NewApi")
    @Test
    public void test_shouldShowRequestBodySensorRationale_never_ask_again() throws Exception {
        PermissionsManager.get()
                .getMockSystemPermissions()
                .neverAskAgain(BODY_SENSORS);
        {
            assertFalse(PermissionsManager.get()
                                .shouldShowBodySensorRationale(new Fragment()));
        }
    }

    // ==== SMS ====================================================================================

    @Test
    public void test_sms_permission_not_granted_by_default() throws Exception {
        {
            assertFalse(PermissionsManager.get()
                                .isSmsGranted());
        }
    }

    @Test
    public void test_neverAskForSms_returns_false_if_user_blocked_us() throws Exception {
        {
            assertFalse(PermissionsManager.get()
                                .neverAskForSms(new Fragment()));
        }

        PermissionsManager.get()
                .requestSmsPermission()
                .subscribe();
        {
            assertFalse(PermissionsManager.get()
                                .neverAskForSms(new Fragment()));
        }

        PermissionsManager.get()
                .requestSmsPermission()
                .subscribe();
        {
            assertFalse(PermissionsManager.get()
                                .neverAskForSms(new Fragment()));
        }

        PermissionsManager.get()
                .getMockSystemPermissions()
                .neverAskAgain(SEND_SMS);
        {
            assertTrue(PermissionsManager.get()
                               .neverAskForSms(new Fragment()));
        }
    }

    @Test
    public void test_neverAskForSms_if_granted() throws Exception {
        PermissionsManager.get()
                .getMockSystemPermissions()
                .setIsSmsGranted(true);
        {
            assertFalse(PermissionsManager.get()
                                .neverAskForSms(new Fragment()));
        }
    }

    @Test
    public void test_shouldShowRequestSmsRationale_not_granted() throws Exception {
        {
            assertFalse(PermissionsManager.get()
                                .shouldShowRequestSmsRationale(new Fragment()));
        }

        PermissionsManager.get()
                .requestSmsPermission()
                .subscribe();
        {
            assertTrue(PermissionsManager.get()
                               .shouldShowRequestSmsRationale(new Fragment()));
        }
    }

    @Test
    public void test_shouldShowRequestSmsRationale_granted() throws Exception {
        PermissionsManager.get()
                .getMockSystemPermissions()
                .setIsSmsGranted(true);
        {
            assertFalse(PermissionsManager.get()
                                .shouldShowRequestSmsRationale(new Fragment()));
        }
    }

    @Test
    public void test_shouldShowRequestSmsRationale_never_ask_again() throws Exception {
        PermissionsManager.get()
                .getMockSystemPermissions()
                .neverAskAgain(SEND_SMS);
        {
            assertFalse(PermissionsManager.get()
                                .shouldShowRequestSmsRationale(new Fragment()));
        }
    }
}