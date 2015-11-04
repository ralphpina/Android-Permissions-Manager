package net.ralphpina.permissionsmanager.sample;

import android.support.test.rule.ActivityTestRule;
import android.util.Log;

import net.ralphpina.permissionsmanager.PermissionsManager;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_CALENDAR;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Android Data Bindings does not seem to play nice with testing. I am using the PermissionManagerStatus
 * as a proxy during testing.
 */
public class MainActivityTest {

    private static final String TAG = "MainActivityTest";

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void setUp() {
        Log.e(TAG, "=== setUp() ===");
        PermissionsManager.clearDb();
        PermissionsManager.get()
                          .injectMockSystemPermissions();
    }

    public void tearDown() {
        PermissionsManager.clearDb();
    }

    // ===== CAMERA ================================================================================

    @Test
    public void correct_ui_for_camera() throws Exception {
        mActivityRule.getActivity()
                     .getPermissionsManagerStatus()
                     .updateStatus(); // fake onResume
        {
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getCameraPermissionStatus(), is("Not given"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getCameraHasAsked(), is("No"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .isCameraNeverAskAgain(), is(false));
        }
    }

    @Test
    public void clicking_button_and_denying_camera_permission() throws Exception {
        onView(withId(R.id.button_camera_permissions)).perform(click());
        mActivityRule.getActivity()
                     .getPermissionsManagerStatus()
                     .updateStatus(); // fake onResume
        {
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getCameraPermissionStatus(), is("Not given"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getCameraHasAsked(), is("Yes"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .isCameraNeverAskAgain(), is(false));
        }
    }

    @Test
    public void clicking_button_and_allowing_camera_permission() throws Exception {
        PermissionsManager.get()
                          .getMockSystemPermissions()
                          .setShouldAllowCameraPermission(true);
        onView(withId(R.id.button_camera_permissions)).perform(click());
        mActivityRule.getActivity()
                     .getPermissionsManagerStatus()
                     .updateStatus(); // fake onResume
        {
            assertTrue(PermissionsManager.get()
                                         .isCameraGranted());
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getCameraPermissionStatus(), is("Given!!!"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getCameraHasAsked(), is("Yes"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .isCameraNeverAskAgain(), is(true));
        }
    }

    @Test
    public void clicking_button_and_setting_never_ask_camera() throws Exception {
        onView(withId(R.id.button_camera_permissions)).perform(click());
        mActivityRule.getActivity()
                     .getPermissionsManagerStatus()
                     .updateStatus(); // fake onResume
        {
            assertFalse(PermissionsManager.get()
                                          .isCameraGranted());
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getCameraPermissionStatus(), is("Not given"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getCameraHasAsked(), is("Yes"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .isCameraNeverAskAgain(), is(false));
        }

        PermissionsManager.get().getMockSystemPermissions().neverAskAgain(CAMERA);
        onView(withId(R.id.button_camera_permissions)).perform(click());
        mActivityRule.getActivity()
                     .getPermissionsManagerStatus()
                     .updateStatus(); // fake onResume
        {
            assertFalse(PermissionsManager.get()
                                          .isCameraGranted());
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getCameraPermissionStatus(), is("Not given"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getCameraHasAsked(), is("Yes"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .isCameraNeverAskAgain(), is(true));
        }
    }

    // ===== LOCATION ================================================================================

    @Test
    public void correct_ui_for_location() throws Exception {
        mActivityRule.getActivity()
                     .getPermissionsManagerStatus()
                     .updateStatus(); // fake onResume
        {
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getLocationPermissionStatus(), is("Not given"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getLocationHasAsked(), is("No"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .isLocationNeverAskAgain(), is(false));
        }
    }

    @Test
    public void clicking_button_and_denying_location_permission() throws Exception {
        onView(withId(R.id.button_location_permissions)).perform(click());
        mActivityRule.getActivity()
                     .getPermissionsManagerStatus()
                     .updateStatus(); // fake onResume
        {
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getLocationPermissionStatus(), is("Not given"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getLocationHasAsked(), is("Yes"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .isLocationNeverAskAgain(), is(false));
        }
    }

    @Test
    public void clicking_button_and_allowing_location_permission() throws Exception {
        PermissionsManager.get()
                          .getMockSystemPermissions()
                          .setShouldAllowLocationPermission(true);
        onView(withId(R.id.button_location_permissions)).perform(click());
        mActivityRule.getActivity()
                     .getPermissionsManagerStatus()
                     .updateStatus(); // fake onResume
        {
            assertTrue(PermissionsManager.get()
                                         .isLocationGranted());
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getLocationPermissionStatus(), is("Given!!!"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getLocationHasAsked(), is("Yes"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .isLocationNeverAskAgain(), is(true));
        }
    }

    @Test
    public void clicking_button_and_setting_never_ask_location() throws Exception {
        onView(withId(R.id.button_location_permissions)).perform(click());
        mActivityRule.getActivity()
                     .getPermissionsManagerStatus()
                     .updateStatus(); // fake onResume
        {
            assertFalse(PermissionsManager.get()
                                          .isLocationGranted());
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getLocationPermissionStatus(), is("Not given"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getLocationHasAsked(), is("Yes"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .isLocationNeverAskAgain(), is(false));
        }

        PermissionsManager.get().getMockSystemPermissions().neverAskAgain(ACCESS_COARSE_LOCATION);
        onView(withId(R.id.button_location_permissions)).perform(click());
        mActivityRule.getActivity()
                     .getPermissionsManagerStatus()
                     .updateStatus(); // fake onResume
        {
            assertFalse(PermissionsManager.get()
                                          .isLocationGranted());
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getLocationPermissionStatus(), is("Not given"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getLocationHasAsked(), is("Yes"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .isLocationNeverAskAgain(), is(true));
        }
    }

    // ===== AUDIO =================================================================================

    @Test
    public void correct_ui_for_audio() throws Exception {
        mActivityRule.getActivity()
                     .getPermissionsManagerStatus()
                     .updateStatus(); // fake onResume
        {
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getAudioPermissionStatus(), is("Not given"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getAudioHasAsked(), is("No"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .isAudioNeverAskAgain(), is(false));
        }
    }

    @Test
    public void clicking_button_and_denying_audio_permission() throws Exception {
        onView(withId(R.id.button_audio_permissions))
                .perform(scrollTo());
        onView(withId(R.id.button_audio_permissions)).perform(click());
        mActivityRule.getActivity()
                     .getPermissionsManagerStatus()
                     .updateStatus(); // fake onResume
        {
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getAudioPermissionStatus(), is("Not given"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getAudioHasAsked(), is("Yes"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .isAudioNeverAskAgain(), is(false));
        }
    }

    @Test
    public void clicking_button_and_allowing_audio_permission() throws Exception {
        PermissionsManager.get()
                          .getMockSystemPermissions()
                          .setShouldAllowAudioRecordingPermission(true);
        onView(withId(R.id.button_audio_permissions))
                .perform(scrollTo());
        onView(withId(R.id.button_audio_permissions)).perform(click());
        mActivityRule.getActivity()
                     .getPermissionsManagerStatus()
                     .updateStatus(); // fake onResume
        {
            assertTrue(PermissionsManager.get()
                                         .isAudioRecordingGranted());
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getAudioPermissionStatus(), is("Given!!!"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getAudioHasAsked(), is("Yes"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .isAudioNeverAskAgain(), is(true));
        }
    }

    @Test
    public void clicking_button_and_setting_never_ask_audio() throws Exception {
        onView(withId(R.id.button_audio_permissions))
                .perform(scrollTo());
        onView(withId(R.id.button_audio_permissions)).perform(click());
        mActivityRule.getActivity()
                     .getPermissionsManagerStatus()
                     .updateStatus(); // fake onResume
        {
            assertFalse(PermissionsManager.get()
                                          .isAudioRecordingGranted());
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getAudioPermissionStatus(), is("Not given"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getAudioHasAsked(), is("Yes"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .isAudioNeverAskAgain(), is(false));
        }

        PermissionsManager.get().getMockSystemPermissions().neverAskAgain(RECORD_AUDIO);
        onView(withId(R.id.button_audio_permissions)).perform(click());
        mActivityRule.getActivity()
                     .getPermissionsManagerStatus()
                     .updateStatus(); // fake onResume
        {
            assertFalse(PermissionsManager.get()
                                          .isAudioRecordingGranted());
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getAudioPermissionStatus(), is("Not given"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getAudioHasAsked(), is("Yes"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .isAudioNeverAskAgain(), is(true));
        }
    }

    // ===== CALENDAR ==============================================================================

    @Test
    public void correct_ui_for_calendar() throws Exception {
        mActivityRule.getActivity()
                     .getPermissionsManagerStatus()
                     .updateStatus(); // fake onResume
        {
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getCalendarPermissionStatus(), is("Not given"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getCalendarHasAsked(), is("No"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .isCalendarNeverAskAgain(), is(false));
        }
    }

    @Test
    public void clicking_button_and_denying_calendar_permission() throws Exception {
        onView(withId(R.id.button_calendar_permissions))
                .perform(scrollTo());
        onView(withId(R.id.button_calendar_permissions)).perform(click());
        mActivityRule.getActivity()
                     .getPermissionsManagerStatus()
                     .updateStatus(); // fake onResume
        {
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getCalendarPermissionStatus(), is("Not given"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getCalendarHasAsked(), is("Yes"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .isCalendarNeverAskAgain(), is(false));
        }
    }

    @Test
    public void clicking_button_and_allowing_calendar_permission() throws Exception {
        PermissionsManager.get()
                          .getMockSystemPermissions()
                          .setShouldAllowCalendarPermission(true);
        onView(withId(R.id.button_calendar_permissions))
                .perform(scrollTo());
        onView(withId(R.id.button_calendar_permissions)).perform(click());
        mActivityRule.getActivity()
                     .getPermissionsManagerStatus()
                     .updateStatus(); // fake onResume
        {
            assertTrue(PermissionsManager.get()
                                         .isCalendarGranted());
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getCalendarPermissionStatus(), is("Given!!!"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getCalendarHasAsked(), is("Yes"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .isCalendarNeverAskAgain(), is(true));
        }
    }

    @Test
    public void clicking_button_and_setting_never_ask_calendar() throws Exception {
        onView(withId(R.id.button_calendar_permissions))
                .perform(scrollTo());
        onView(withId(R.id.button_calendar_permissions)).perform(click());
        mActivityRule.getActivity()
                     .getPermissionsManagerStatus()
                     .updateStatus(); // fake onResume
        {
            assertFalse(PermissionsManager.get()
                                          .isCalendarGranted());
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getCalendarPermissionStatus(), is("Not given"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getCalendarHasAsked(), is("Yes"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .isCalendarNeverAskAgain(), is(false));
        }

        PermissionsManager.get().getMockSystemPermissions().neverAskAgain(READ_CALENDAR);
        onView(withId(R.id.button_calendar_permissions)).perform(click());
        mActivityRule.getActivity()
                     .getPermissionsManagerStatus()
                     .updateStatus(); // fake onResume
        {
            assertFalse(PermissionsManager.get()
                                          .isCalendarGranted());
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getCalendarPermissionStatus(), is("Not given"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getCalendarHasAsked(), is("Yes"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .isCalendarNeverAskAgain(), is(true));
        }
    }

    // ===== CONTACTS ==============================================================================

    @Test
    public void correct_ui_for_contacts() throws Exception {
        mActivityRule.getActivity()
                     .getPermissionsManagerStatus()
                     .updateStatus(); // fake onResume
        {
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getContactsPermissionStatus(), is("Not given"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getContactsHasAsked(), is("No"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .isContactsNeverAskAgain(), is(false));
        }
    }

    @Test
    public void clicking_button_and_denying_contacts_permission() throws Exception {
        onView(withId(R.id.button_contacts_permissions))
                .perform(scrollTo());
        onView(withId(R.id.button_contacts_permissions)).perform(click());
        mActivityRule.getActivity()
                     .getPermissionsManagerStatus()
                     .updateStatus(); // fake onResume
        {
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getContactsPermissionStatus(), is("Not given"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getContactsHasAsked(), is("Yes"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .isContactsNeverAskAgain(), is(false));
        }
    }

    @Test
    public void clicking_button_and_allowing_contacts_permission() throws Exception {
        PermissionsManager.get()
                          .getMockSystemPermissions()
                          .setShouldAllowContactsPermission(true);
        onView(withId(R.id.button_contacts_permissions))
                .perform(scrollTo());
        onView(withId(R.id.button_contacts_permissions)).perform(click());
        mActivityRule.getActivity()
                     .getPermissionsManagerStatus()
                     .updateStatus(); // fake onResume
        {
            assertTrue(PermissionsManager.get()
                                         .isContactsGranted());
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getContactsPermissionStatus(), is("Given!!!"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getContactsHasAsked(), is("Yes"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .isContactsNeverAskAgain(), is(true));
        }
    }

    @Test
    public void clicking_button_and_setting_never_ask_contacts() throws Exception {
        onView(withId(R.id.button_contacts_permissions))
                .perform(scrollTo());
        onView(withId(R.id.button_contacts_permissions)).perform(click());
        mActivityRule.getActivity()
                     .getPermissionsManagerStatus()
                     .updateStatus(); // fake onResume
        {
            assertFalse(PermissionsManager.get()
                                          .isContactsGranted());
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getContactsPermissionStatus(), is("Not given"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getContactsHasAsked(), is("Yes"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .isContactsNeverAskAgain(), is(false));
        }

        PermissionsManager.get().getMockSystemPermissions().neverAskAgain(READ_CONTACTS);
        onView(withId(R.id.button_contacts_permissions)).perform(click());
        mActivityRule.getActivity()
                     .getPermissionsManagerStatus()
                     .updateStatus(); // fake onResume
        {
            assertFalse(PermissionsManager.get()
                                          .isContactsGranted());
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getContactsPermissionStatus(), is("Not given"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getContactsHasAsked(), is("Yes"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .isContactsNeverAskAgain(), is(true));
        }
    }

    // ===== CALLING ===============================================================================

    @Test
    public void correct_ui_for_calling() throws Exception {
        mActivityRule.getActivity()
                     .getPermissionsManagerStatus()
                     .updateStatus(); // fake onResume
        {
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getCallingPermissionStatus(), is("Not given"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getCallingHasAsked(), is("No"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .isCallingNeverAskAgain(), is(false));
        }
    }

    @Test
    public void clicking_button_and_denying_calling_permission() throws Exception {
        onView(withId(R.id.button_calling_permissions))
                .perform(scrollTo());
        onView(withId(R.id.button_calling_permissions)).perform(click());
        mActivityRule.getActivity()
                     .getPermissionsManagerStatus()
                     .updateStatus(); // fake onResume
        {
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getCallingPermissionStatus(), is("Not given"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getCallingHasAsked(), is("Yes"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .isCallingNeverAskAgain(), is(false));
        }
    }

    @Test
    public void clicking_button_and_allowing_calling_permission() throws Exception {
        PermissionsManager.get()
                          .getMockSystemPermissions()
                          .setShouldAllowCallingPermission(true);
        onView(withId(R.id.button_calling_permissions))
                .perform(scrollTo());
        onView(withId(R.id.button_calling_permissions)).perform(click());
        mActivityRule.getActivity()
                     .getPermissionsManagerStatus()
                     .updateStatus(); // fake onResume
        {
            assertTrue(PermissionsManager.get()
                                         .isCallingGranted());
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getCallingPermissionStatus(), is("Given!!!"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getCallingHasAsked(), is("Yes"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .isCallingNeverAskAgain(), is(true));
        }
    }

    @Test
    public void clicking_button_and_setting_never_ask_calling() throws Exception {
        onView(withId(R.id.button_calling_permissions))
                .perform(scrollTo());
        onView(withId(R.id.button_calling_permissions)).perform(click());
        mActivityRule.getActivity()
                     .getPermissionsManagerStatus()
                     .updateStatus(); // fake onResume
        {
            assertFalse(PermissionsManager.get()
                                          .isCallingGranted());
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getCallingPermissionStatus(), is("Not given"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getCallingHasAsked(), is("Yes"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .isCallingNeverAskAgain(), is(false));
        }

        PermissionsManager.get().getMockSystemPermissions().neverAskAgain(CALL_PHONE);
        onView(withId(R.id.button_calling_permissions)).perform(click());
        mActivityRule.getActivity()
                     .getPermissionsManagerStatus()
                     .updateStatus(); // fake onResume
        {
            assertFalse(PermissionsManager.get()
                                          .isCallingGranted());
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getCallingPermissionStatus(), is("Not given"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getCallingHasAsked(), is("Yes"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .isCallingNeverAskAgain(), is(true));
        }
    }

    // ===== STORAGE ===============================================================================

    @Test
    public void correct_ui_for_storage() throws Exception {
        mActivityRule.getActivity()
                     .getPermissionsManagerStatus()
                     .updateStatus(); // fake onResume
        {
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getStoragePermissionStatus(), is("Not given"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getStorageHasAsked(), is("No"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .isStorageNeverAskAgain(), is(false));
        }
    }

    @Test
    public void clicking_button_and_denying_storage_permission() throws Exception {
        onView(withId(R.id.button_storage_permissions))
                .perform(scrollTo());
        onView(withId(R.id.button_storage_permissions)).perform(click());
        mActivityRule.getActivity()
                     .getPermissionsManagerStatus()
                     .updateStatus(); // fake onResume
        {
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getStoragePermissionStatus(), is("Not given"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getStorageHasAsked(), is("Yes"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .isStorageNeverAskAgain(), is(false));
        }
    }

    @Test
    public void clicking_button_and_allowing_storage_permission() throws Exception {
        PermissionsManager.get()
                          .getMockSystemPermissions()
                          .setShouldAllowStoragePermission(true);
        onView(withId(R.id.button_storage_permissions))
                .perform(scrollTo());
        onView(withId(R.id.button_storage_permissions)).perform(click());
        mActivityRule.getActivity()
                     .getPermissionsManagerStatus()
                     .updateStatus(); // fake onResume
        {
            assertTrue(PermissionsManager.get()
                                         .isStorageGranted());
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getStoragePermissionStatus(), is("Given!!!"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getStorageHasAsked(), is("Yes"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .isStorageNeverAskAgain(), is(true));
        }
    }

    @Test
    public void clicking_button_and_setting_never_ask_storage() throws Exception {
        onView(withId(R.id.button_storage_permissions))
                .perform(scrollTo());
        onView(withId(R.id.button_storage_permissions)).perform(click());
        mActivityRule.getActivity()
                     .getPermissionsManagerStatus()
                     .updateStatus(); // fake onResume
        {
            assertFalse(PermissionsManager.get()
                                          .isStorageGranted());
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getStoragePermissionStatus(), is("Not given"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getStorageHasAsked(), is("Yes"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .isStorageNeverAskAgain(), is(false));
        }

        PermissionsManager.get().getMockSystemPermissions().neverAskAgain(WRITE_EXTERNAL_STORAGE);
        onView(withId(R.id.button_storage_permissions)).perform(click());
        mActivityRule.getActivity()
                     .getPermissionsManagerStatus()
                     .updateStatus(); // fake onResume
        {
            assertFalse(PermissionsManager.get()
                                          .isCallingGranted());
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getStoragePermissionStatus(), is("Not given"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .getStorageHasAsked(), is("Yes"));
            assertThat(mActivityRule.getActivity()
                                    .getPermissionsManagerStatus()
                                    .isStorageNeverAskAgain(), is(true));
        }
    }
}
