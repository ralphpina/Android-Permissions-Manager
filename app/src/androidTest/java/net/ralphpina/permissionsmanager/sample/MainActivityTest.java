package net.ralphpina.permissionsmanager.sample;

import android.support.test.rule.ActivityTestRule;

import net.ralphpina.permissionsmanager.PermissionsManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.BODY_SENSORS;
import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_CALENDAR;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.SEND_SMS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Android Data Bindings does not seem to play nice with testing. I am using the PermissionManagerStatus
 * as a proxy during testing.
 */
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class,
            false,
            false);

    @Before
    public void setUp() {
        PermissionsManager.clearDb();
        PermissionsManager.get()
                .injectMockSystemPermissions();
    }

    @After
    public void tearDown() {
        PermissionsManager.clearDb();
    }

    // ===== CAMERA ================================================================================

    @Test
    public void correct_ui_for_camera() throws Exception {
        mActivityRule.launchActivity(null);
        onView(withId(R.id.button_camera_permissions)).check(matches(withText(
                "Request Camera Permission")));
        onView(withId(R.id.camera_permissions_status)).check(matches(withText("Not given")));
        onView(withId(R.id.camera_permissions_has_asked)).check(matches(withText("No")));
        onView(withId(R.id.camera_permissions_never_again)).check(matches(withText("No")));
    }

    @Test
    public void clicking_button_and_denying_camera_permission() throws Exception {
        mActivityRule.launchActivity(null);
        onView(withId(R.id.button_camera_permissions)).perform(click());

        onView(withId(R.id.camera_permissions_status)).check(matches(withText("Not given")));
        onView(withId(R.id.camera_permissions_has_asked)).check(matches(withText("Yes")));
        onView(withId(R.id.camera_permissions_never_again)).check(matches(withText("No")));
    }

    @Test
    public void clicking_button_and_allowing_camera_permission() throws Exception {
        PermissionsManager.get()
                .getMockSystemPermissions()
                .setShouldAllowCameraPermission(true);
        mActivityRule.launchActivity(null);
        onView(withId(R.id.button_camera_permissions)).perform(click());
        onView(withId(R.id.camera_permissions_status)).check(matches(withText("Given!!!")));
        onView(withId(R.id.camera_permissions_has_asked)).check(matches(withText("Yes")));
        onView(withId(R.id.camera_permissions_never_again)).check(matches(withText("Yes")));
    }

    @Test
    public void clicking_button_and_setting_never_ask_camera() throws Exception {
        mActivityRule.launchActivity(null);
        onView(withId(R.id.button_camera_permissions)).perform(click());
        mActivityRule.getActivity()
                .getPermissionsManagerStatus()
                .updateStatus(); // fake onResume
        {

            assertFalse(PermissionsManager.get()
                                .isCameraGranted());
            onView(withId(R.id.camera_permissions_status)).check(matches(withText("Not given")));
            onView(withId(R.id.camera_permissions_has_asked)).check(matches(withText("Yes")));
            onView(withId(R.id.camera_permissions_never_again)).check(matches(withText("No")));
        }

        PermissionsManager.get()
                .getMockSystemPermissions()
                .neverAskAgain(CAMERA);
        onView(withId(R.id.button_camera_permissions)).perform(click());
        // fake asking
        mActivityRule.getActivity()
                .getPermissionsManagerStatus()
                .updateStatus();
        onView(withId(R.id.camera_permissions_status)).check(matches(withText("Not given")));
        onView(withId(R.id.camera_permissions_has_asked)).check(matches(withText("Yes")));
        synchronized (this) {
            wait(100);
        }
        onView(withId(R.id.camera_permissions_never_again)).check(matches(withText("Yes")));
    }

    // ===== LOCATION ================================================================================

    @Test
    public void correct_ui_for_location() throws Exception {
        mActivityRule.launchActivity(null);
        {
            onView(withId(R.id.location_permissions_status)).check(matches(withText("Not given")));
            onView(withId(R.id.location_permissions_has_asked)).check(matches(withText("No")));
            onView(withId(R.id.location_permissions_never_again)).check(matches(withText("No")));
        }
    }

    @Test
    public void clicking_button_and_denying_location_permission() throws Exception {
        mActivityRule.launchActivity(null);

        onView(withId(R.id.button_location_permissions)).perform(scrollTo(), click());
        {
            onView(withId(R.id.location_permissions_status)).check(matches(withText("Not given")));
            onView(withId(R.id.location_permissions_has_asked)).check(matches(withText("Yes")));
            onView(withId(R.id.location_permissions_never_again)).check(matches(withText("No")));
        }
    }

    @Test
    public void clicking_button_and_allowing_location_permission() throws Exception {
        PermissionsManager.get()
                .getMockSystemPermissions()
                .setShouldAllowLocationPermission(true);
        mActivityRule.launchActivity(null);
        onView(withId(R.id.button_location_permissions)).perform(scrollTo(), click());
        {
            onView(withId(R.id.location_permissions_status)).check(matches(withText("Given!!!")));
            onView(withId(R.id.location_permissions_has_asked)).check(matches(withText("Yes")));
            onView(withId(R.id.location_permissions_never_again)).check(matches(withText("Yes")));
        }
    }

    @Test
    public void clicking_button_and_setting_never_ask_location() throws Exception {
        mActivityRule.launchActivity(null);
        onView(withId(R.id.button_location_permissions)).perform(scrollTo(), click());
        {
            onView(withId(R.id.location_permissions_status)).check(matches(withText("Not given")));
            onView(withId(R.id.location_permissions_has_asked)).check(matches(withText("Yes")));
            onView(withId(R.id.location_permissions_never_again)).check(matches(withText("No")));
        }

        PermissionsManager.get()
                .getMockSystemPermissions()
                .neverAskAgain(ACCESS_COARSE_LOCATION);
        onView(withId(R.id.button_location_permissions)).perform(click());
        mActivityRule.getActivity()
                .getPermissionsManagerStatus()
                .updateStatus(); // fake onResume
        {
            onView(withId(R.id.location_permissions_status)).check(matches(withText("Not given")));
            onView(withId(R.id.location_permissions_has_asked)).check(matches(withText("Yes")));
            synchronized (this) {
                wait(100);
            }
            onView(withId(R.id.location_permissions_never_again)).check(matches(withText("Yes")));
        }
    }

    // ===== MICROPHONE ============================================================================

    @Test
    public void correct_ui_for_audio() throws Exception {
        mActivityRule.launchActivity(null);
        {
            onView(withId(R.id.audio_permissions_status)).check(matches(withText("Not given")));
            onView(withId(R.id.audio_permissions_has_asked)).check(matches(withText("No")));
            onView(withId(R.id.audio_permissions_never_again)).check(matches(withText("No")));
        }
    }

    @Test
    public void clicking_button_and_denying_audio_permission() throws Exception {
        mActivityRule.launchActivity(null);
        onView(withId(R.id.button_audio_permissions)).perform(scrollTo(), click());
        {
            onView(withId(R.id.audio_permissions_status)).check(matches(withText("Not given")));
            onView(withId(R.id.audio_permissions_has_asked)).check(matches(withText("Yes")));
            onView(withId(R.id.audio_permissions_never_again)).check(matches(withText("No")));
        }
    }

    @Test
    public void clicking_button_and_allowing_audio_permission() throws Exception {
        PermissionsManager.get()
                .getMockSystemPermissions()
                .setShouldAllowAudioRecordingPermission(true);
        mActivityRule.launchActivity(null);
        onView(withId(R.id.button_audio_permissions)).perform(scrollTo(), click());
        {
            onView(withId(R.id.audio_permissions_status)).check(matches(withText("Given!!!")));
            onView(withId(R.id.audio_permissions_has_asked)).check(matches(withText("Yes")));
            onView(withId(R.id.audio_permissions_never_again)).check(matches(withText("Yes")));
        }
    }

    @Test
    public void clicking_button_and_setting_never_ask_audio() throws Exception {
        mActivityRule.launchActivity(null);
        onView(withId(R.id.button_audio_permissions)).perform(scrollTo(), click());
        {
            onView(withId(R.id.audio_permissions_status)).check(matches(withText("Not given")));
            onView(withId(R.id.audio_permissions_has_asked)).check(matches(withText("Yes")));
            onView(withId(R.id.audio_permissions_never_again)).check(matches(withText("No")));
        }

        PermissionsManager.get()
                .getMockSystemPermissions()
                .neverAskAgain(RECORD_AUDIO);
        onView(withId(R.id.button_audio_permissions)).perform(click());
        mActivityRule.getActivity()
                .getPermissionsManagerStatus()
                .updateStatus(); // fake onResume
        {
            onView(withId(R.id.audio_permissions_status)).check(matches(withText("Not given")));
            onView(withId(R.id.audio_permissions_has_asked)).check(matches(withText("Yes")));
            synchronized (this) {
                wait(100);
            }
            onView(withId(R.id.audio_permissions_never_again)).check(matches(withText("Yes")));
        }
    }

    // ===== CALENDAR ==============================================================================

    @Test
    public void correct_ui_for_calendar() throws Exception {
        mActivityRule.launchActivity(null);
        {
            onView(withId(R.id.calendar_permissions_status)).check(matches(withText("Not given")));
            onView(withId(R.id.calendar_permissions_has_asked)).check(matches(withText("No")));
            onView(withId(R.id.calendar_permissions_never_again)).check(matches(withText("No")));
        }
    }

    @Test
    public void clicking_button_and_denying_calendar_permission() throws Exception {
        mActivityRule.launchActivity(null);
        onView(withId(R.id.button_calendar_permissions)).perform(scrollTo(), click());
        {
            onView(withId(R.id.calendar_permissions_status)).check(matches(withText("Not given")));
            onView(withId(R.id.calendar_permissions_has_asked)).check(matches(withText("Yes")));
            onView(withId(R.id.calendar_permissions_never_again)).check(matches(withText("No")));
        }
    }

    @Test
    public void clicking_button_and_allowing_calendar_permission() throws Exception {
        PermissionsManager.get()
                .getMockSystemPermissions()
                .setShouldAllowCalendarPermission(true);
        mActivityRule.launchActivity(null);
        onView(withId(R.id.button_calendar_permissions)).perform(scrollTo(), click());
        {
            onView(withId(R.id.calendar_permissions_status)).check(matches(withText("Given!!!")));
            onView(withId(R.id.calendar_permissions_has_asked)).check(matches(withText("Yes")));
            onView(withId(R.id.calendar_permissions_never_again)).check(matches(withText("Yes")));
        }
    }

    @Test
    public void clicking_button_and_setting_never_ask_calendar() throws Exception {
        mActivityRule.launchActivity(null);
        onView(withId(R.id.button_calendar_permissions)).perform(scrollTo(), click());
        mActivityRule.getActivity()
                .getPermissionsManagerStatus()
                .updateStatus(); // fake onResume
        {
            onView(withId(R.id.calendar_permissions_status)).check(matches(withText("Not given")));
            onView(withId(R.id.calendar_permissions_has_asked)).check(matches(withText("Yes")));
            onView(withId(R.id.calendar_permissions_never_again)).check(matches(withText("No")));
        }

        PermissionsManager.get()
                .getMockSystemPermissions()
                .neverAskAgain(READ_CALENDAR);
        onView(withId(R.id.button_calendar_permissions)).perform(click());
        mActivityRule.getActivity()
                .getPermissionsManagerStatus()
                .updateStatus(); // fake onResume
        {
            onView(withId(R.id.calendar_permissions_status)).check(matches(withText("Not given")));
            onView(withId(R.id.calendar_permissions_has_asked)).check(matches(withText("Yes")));
            synchronized (this) {
                wait(100);
            }
            onView(withId(R.id.calendar_permissions_never_again)).check(matches(withText("Yes")));
        }
    }

    // ===== CONTACTS ==============================================================================

    @Test
    public void correct_ui_for_contacts() throws Exception {
        mActivityRule.launchActivity(null);
        {
            onView(withId(R.id.contacts_permissions_status)).check(matches(withText("Not given")));
            onView(withId(R.id.contacts_permissions_has_asked)).check(matches(withText("No")));
            onView(withId(R.id.contacts_permissions_never_again)).check(matches(withText("No")));
        }
    }

    @Test
    public void clicking_button_and_denying_contacts_permission() throws Exception {
        mActivityRule.launchActivity(null);
        onView(withId(R.id.button_contacts_permissions)).perform(scrollTo(), click());
        {
            onView(withId(R.id.contacts_permissions_status)).check(matches(withText("Not given")));
            onView(withId(R.id.contacts_permissions_has_asked)).check(matches(withText("Yes")));
            onView(withId(R.id.contacts_permissions_never_again)).check(matches(withText("No")));
        }
    }

    @Test
    public void clicking_button_and_allowing_contacts_permission() throws Exception {
        PermissionsManager.get()
                .getMockSystemPermissions()
                .setShouldAllowContactsPermission(true);
        mActivityRule.launchActivity(null);
        onView(withId(R.id.button_contacts_permissions)).perform(scrollTo(), click());
        {
            onView(withId(R.id.contacts_permissions_status)).check(matches(withText("Given!!!")));
            onView(withId(R.id.contacts_permissions_has_asked)).check(matches(withText("Yes")));
            onView(withId(R.id.contacts_permissions_never_again)).check(matches(withText("Yes")));
        }
    }

    @Test
    public void clicking_button_and_setting_never_ask_contacts() throws Exception {
        mActivityRule.launchActivity(null);
        onView(withId(R.id.button_contacts_permissions)).perform(scrollTo(), click());
        {
            assertFalse(PermissionsManager.get()
                                .isContactsGranted());
            onView(withId(R.id.contacts_permissions_status)).check(matches(withText("Not given")));
            onView(withId(R.id.contacts_permissions_has_asked)).check(matches(withText("Yes")));
            onView(withId(R.id.contacts_permissions_never_again)).check(matches(withText("No")));
        }

        PermissionsManager.get()
                .getMockSystemPermissions()
                .neverAskAgain(READ_CONTACTS);
        onView(withId(R.id.button_contacts_permissions)).perform(click());
        mActivityRule.getActivity()
                .getPermissionsManagerStatus()
                .updateStatus(); // fake onResume
        {
            onView(withId(R.id.contacts_permissions_status)).check(matches(withText("Not given")));
            onView(withId(R.id.contacts_permissions_has_asked)).check(matches(withText("Yes")));
            synchronized (this) {
                wait(100);
            }
            onView(withId(R.id.contacts_permissions_never_again)).check(matches(withText("Yes")));
        }
    }

    // ===== CALLING ===============================================================================

    @Test
    public void correct_ui_for_calling() throws Exception {
        mActivityRule.launchActivity(null);
        {
            onView(withId(R.id.calling_permissions_status)).check(matches(withText("Not given")));
            onView(withId(R.id.calling_permissions_has_asked)).check(matches(withText("No")));
            onView(withId(R.id.calling_permissions_never_again)).check(matches(withText("No")));
        }
    }

    @Test
    public void clicking_button_and_denying_calling_permission() throws Exception {
        mActivityRule.launchActivity(null);
        onView(withId(R.id.button_calling_permissions)).perform(scrollTo(), click());
        {
            onView(withId(R.id.calling_permissions_status)).check(matches(withText("Not given")));
            onView(withId(R.id.calling_permissions_has_asked)).check(matches(withText("Yes")));
            onView(withId(R.id.calling_permissions_never_again)).check(matches(withText("No")));
        }
    }

    @Test
    public void clicking_button_and_allowing_calling_permission() throws Exception {
        PermissionsManager.get()
                .getMockSystemPermissions()
                .setShouldAllowCallingPermission(true);
        mActivityRule.launchActivity(null);

        onView(withId(R.id.button_calling_permissions)).perform(scrollTo(), click());
        {
            assertTrue(PermissionsManager.get()
                               .isPhoneGranted());
            onView(withId(R.id.calling_permissions_status)).check(matches(withText("Given!!!")));
            onView(withId(R.id.calling_permissions_has_asked)).check(matches(withText("Yes")));
            onView(withId(R.id.calling_permissions_never_again)).check(matches(withText("Yes")));
        }
    }

    @Test
    public void clicking_button_and_setting_never_ask_calling() throws Exception {
        mActivityRule.launchActivity(null);
        onView(withId(R.id.button_calling_permissions)).perform(scrollTo(), click());
        {
            assertFalse(PermissionsManager.get()
                                .isPhoneGranted());
            onView(withId(R.id.calling_permissions_status)).check(matches(withText("Not given")));
            onView(withId(R.id.calling_permissions_has_asked)).check(matches(withText("Yes")));
            onView(withId(R.id.calling_permissions_never_again)).check(matches(withText("No")));
        }

        PermissionsManager.get()
                .getMockSystemPermissions()
                .neverAskAgain(CALL_PHONE);
        onView(withId(R.id.button_calling_permissions)).perform(click());
        mActivityRule.getActivity()
                .getPermissionsManagerStatus()
                .updateStatus(); // fake onResume
        {
            onView(withId(R.id.calling_permissions_status)).check(matches(withText("Not given")));
            onView(withId(R.id.calling_permissions_has_asked)).check(matches(withText("Yes")));
            synchronized (this) {
                wait(100);
            }
            onView(withId(R.id.calling_permissions_never_again)).check(matches(withText("Yes")));
        }
    }

    // ===== STORAGE ===============================================================================

    @Test
    public void correct_ui_for_storage() throws Exception {
        mActivityRule.launchActivity(null);
        {
            onView(withId(R.id.storage_permissions_status)).check(matches(withText("Not given")));
            onView(withId(R.id.storage_permissions_has_asked)).check(matches(withText("No")));
            onView(withId(R.id.storage_permissions_never_again)).check(matches(withText("No")));
        }
    }

    @Test
    public void clicking_button_and_denying_storage_permission() throws Exception {
        mActivityRule.launchActivity(null);
        onView(withId(R.id.button_storage_permissions)).perform(scrollTo(), click());
        {
            onView(withId(R.id.storage_permissions_status)).check(matches(withText("Not given")));
            onView(withId(R.id.storage_permissions_has_asked)).check(matches(withText("Yes")));
            onView(withId(R.id.storage_permissions_never_again)).check(matches(withText("No")));
        }
    }

    @Test
    public void clicking_button_and_allowing_storage_permission() throws Exception {
        PermissionsManager.get()
                .getMockSystemPermissions()
                .setShouldAllowStoragePermission(true);
        mActivityRule.launchActivity(null);

        onView(withId(R.id.button_storage_permissions))
                .perform(scrollTo(), click());
        {
            onView(withId(R.id.storage_permissions_status)).check(matches(withText("Given!!!")));
            onView(withId(R.id.storage_permissions_has_asked)).check(matches(withText("Yes")));
            onView(withId(R.id.storage_permissions_never_again)).check(matches(withText("Yes")));
        }
    }

    @Test
    public void clicking_button_and_setting_never_ask_storage() throws Exception {
        mActivityRule.launchActivity(null);

        onView(withId(R.id.button_storage_permissions)).perform(scrollTo(), click());
        {

            assertFalse(PermissionsManager.get()
                                .isStorageGranted());
            onView(withId(R.id.storage_permissions_status)).check(matches(withText("Not given")));
            onView(withId(R.id.storage_permissions_has_asked)).check(matches(withText("Yes")));
            onView(withId(R.id.storage_permissions_never_again)).check(matches(withText("No")));
        }

        PermissionsManager.get()
                .getMockSystemPermissions()
                .neverAskAgain(WRITE_EXTERNAL_STORAGE);
        mActivityRule.getActivity()
                .getPermissionsManagerStatus()
                .updateStatus(); // fake onResume
        onView(withId(R.id.button_storage_permissions)).perform(click());
        onView(withId(R.id.storage_permissions_status)).check(matches(withText("Not given")));
        onView(withId(R.id.storage_permissions_has_asked)).check(matches(withText("Yes")));
        synchronized (this) {
            wait(100);
        }
        onView(withId(R.id.storage_permissions_never_again)).check(matches(withText("Yes")));
    }

    // ===== BODY SENSOR ================================================================================

    @Test
    public void correct_ui_for_body_sensor() throws Exception {
        mActivityRule.launchActivity(null);
        onView(withId(R.id.button_body_sensor_permission)).perform(scrollTo())
                .check(matches(withText(
                        "Request Body Sensor Permission")));
        onView(withId(R.id.body_sensor_permissions_status)).check(matches(withText("Not given")));
        onView(withId(R.id.body_sensor_permissions_has_asked)).check(matches(withText("No")));
        onView(withId(R.id.body_sensor_permissions_never_again)).check(matches(withText("No")));
    }

    @Test
    public void clicking_button_and_denying_body_sensor_permission() throws Exception {
        mActivityRule.launchActivity(null);
        onView(withId(R.id.button_body_sensor_permission)).perform(scrollTo(), click());

        onView(withId(R.id.body_sensor_permissions_status)).check(matches(withText("Not given")));
        onView(withId(R.id.body_sensor_permissions_has_asked)).check(matches(withText("Yes")));
        onView(withId(R.id.body_sensor_permissions_never_again)).check(matches(withText("No")));
    }

    @Test
    public void clicking_button_and_allowing_body_sensor_permission() throws Exception {
        PermissionsManager.get()
                .getMockSystemPermissions()
                .setShouldAllowBodySensorsPermission(true);
        mActivityRule.launchActivity(null);
        onView(withId(R.id.button_body_sensor_permission)).perform(scrollTo(), click());
        onView(withId(R.id.body_sensor_permissions_status)).check(matches(withText("Given!!!")));
        onView(withId(R.id.body_sensor_permissions_has_asked)).check(matches(withText("Yes")));
        onView(withId(R.id.body_sensor_permissions_never_again)).check(matches(withText("Yes")));
    }

    @SuppressWarnings("NewApi")
    @Test
    public void clicking_button_and_setting_never_ask_body_sensor() throws Exception {
        mActivityRule.launchActivity(null);
        onView(withId(R.id.button_body_sensor_permission)).perform(scrollTo(), click());
        {

            assertFalse(PermissionsManager.get()
                                .isBodySensorGranted());
            onView(withId(R.id.body_sensor_permissions_status)).check(matches(withText("Not given")));
            onView(withId(R.id.body_sensor_permissions_has_asked)).check(matches(withText("Yes")));
            onView(withId(R.id.body_sensor_permissions_never_again)).check(matches(withText("No")));
        }

        PermissionsManager.get()
                .getMockSystemPermissions()
                .neverAskAgain(BODY_SENSORS);
        mActivityRule.getActivity()
                .getPermissionsManagerStatus()
                .updateStatus(); // fake onResume
        onView(withId(R.id.button_body_sensor_permission)).perform(click());
        onView(withId(R.id.body_sensor_permissions_status)).check(matches(withText("Not given")));
        onView(withId(R.id.body_sensor_permissions_has_asked)).check(matches(withText("Yes")));
        synchronized (this) {
            wait(100);
        }
        onView(withId(R.id.body_sensor_permissions_never_again)).check(matches(withText("Yes")));
    }

    // ===== SMS ================================================================================

    @Test
    public void correct_ui_for_sms() throws Exception {
        mActivityRule.launchActivity(null);
        onView(withId(R.id.button_sms_permissions)).perform(scrollTo())
                .check(matches(withText(
                        "Request SMS Permission")));
        onView(withId(R.id.sms_permissions_status)).check(matches(withText("Not given")));
        onView(withId(R.id.sms_permissions_has_asked)).check(matches(withText("No")));
        onView(withId(R.id.sms_permissions_never_again)).check(matches(withText("No")));
    }

    @Test
    public void clicking_button_and_denying_sms_permission() throws Exception {
        mActivityRule.launchActivity(null);
        onView(withId(R.id.button_sms_permissions)).perform(scrollTo(), click());

        onView(withId(R.id.sms_permissions_status)).check(matches(withText("Not given")));
        onView(withId(R.id.sms_permissions_has_asked)).check(matches(withText("Yes")));
        onView(withId(R.id.sms_permissions_never_again)).check(matches(withText("No")));
    }

    @Test
    public void clicking_button_and_allowing_sms_permission() throws Exception {
        PermissionsManager.get()
                .getMockSystemPermissions()
                .setShouldAllowSmsPermission(true);
        mActivityRule.launchActivity(null);
        onView(withId(R.id.button_sms_permissions)).perform(scrollTo(), click());
        onView(withId(R.id.sms_permissions_status)).check(matches(withText("Given!!!")));
        onView(withId(R.id.sms_permissions_has_asked)).check(matches(withText("Yes")));
        synchronized (this) {
            wait(100);
        }
        onView(withId(R.id.sms_permissions_never_again)).check(matches(withText("Yes")));
    }

    @Test
    public void clicking_button_and_setting_never_ask_sms() throws Exception {
        mActivityRule.launchActivity(null);
        onView(withId(R.id.button_sms_permissions)).perform(scrollTo(), click());
        {

            assertFalse(PermissionsManager.get()
                                .isSmsGranted());
            onView(withId(R.id.sms_permissions_status)).check(matches(withText("Not given")));
            onView(withId(R.id.sms_permissions_has_asked)).check(matches(withText("Yes")));
            onView(withId(R.id.sms_permissions_never_again)).check(matches(withText("No")));
        }

        PermissionsManager.get()
                .getMockSystemPermissions()
                .neverAskAgain(SEND_SMS);
        mActivityRule.getActivity()
                .getPermissionsManagerStatus()
                .updateStatus(); // fake onResume
        onView(withId(R.id.button_sms_permissions)).perform(scrollTo(), click());
        onView(withId(R.id.sms_permissions_status)).check(matches(withText("Not given")));
        onView(withId(R.id.sms_permissions_has_asked)).check(matches(withText("Yes")));
        synchronized (this) {
            wait(100);
        }
        onView(withId(R.id.sms_permissions_never_again)).check(matches(withText("Yes")));
    }
}
