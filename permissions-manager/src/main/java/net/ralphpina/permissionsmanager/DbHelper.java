package net.ralphpina.permissionsmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class DbHelper {

    private static final String HAS_ASKED_FOR_CAMERA_KEY          = "has_asked_for_camera";
    private static final String HAS_ASKED_FOR_LOCATION_KEY        = "has_asked_for_location";
    private static final String HAS_ASKED_FOR_AUDIO_RECORDING_KEY = "has_asked_for_audio_recording";
    private static final String HAS_ASKED_FOR_CALENDAR_KEY        = "has_asked_for_calendar";
    private static final String HAS_ASKED_FOR_CONTACTS_KEY        = "has_asked_for_contacts";
    private static final String HAS_ASKED_FOR_CALLING_KEY         = "has_asked_for_calling";
    private static final String HAS_ASKED_FOR_STORAGE_KEY         = "has_asked_for_storage";
    private static final String HAS_ASKED_FOR_BODY_SENSORS_KEY    = "has_asked_for_body_sensors";
    private static final String HAS_ASKED_FOR_SMS_KEY             = "has_asked_for_sms";

    private static DbHelper          mInstance;
    private final  SharedPreferences preferences;

    static void init(Context context) {
        mInstance = new DbHelper(context);
    }

    void tearDown() {
        clearData();
        mInstance = null;
    }

    private DbHelper(Context context) {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    static DbHelper get() {
        return mInstance;
    }

    // ==== CAMERA =================================================================================

    void setCameraPermissionsAsked() {
        preferences.edit()
                .putBoolean(HAS_ASKED_FOR_CAMERA_KEY, true)
                .apply();
    }

    boolean isCameraPermissionsAsked() {
        return preferences.getBoolean(HAS_ASKED_FOR_CAMERA_KEY, false);
    }

    // ===== LOCATION ==============================================================================

    void setLocationPermissionsAsked() {
        preferences.edit()
                .putBoolean(HAS_ASKED_FOR_LOCATION_KEY, true)
                .apply();
    }

    boolean isLocationPermissionsAsked() {
        return preferences.getBoolean(HAS_ASKED_FOR_LOCATION_KEY, false);
    }

    // ===== MICROPHONE ============================================================================

    void setMicrophonePermissionsAsked() {
        preferences.edit()
                .putBoolean(HAS_ASKED_FOR_AUDIO_RECORDING_KEY, true)
                .apply();
    }

    boolean isAudioPermissionsAsked() {
        return preferences.getBoolean(HAS_ASKED_FOR_AUDIO_RECORDING_KEY, false);
    }

    // ===== CALENDAR ==============================================================================

    void setCalendarPermissionsAsked() {
        preferences.edit()
                .putBoolean(HAS_ASKED_FOR_CALENDAR_KEY, true)
                .apply();
    }

    boolean isCalendarPermissionsAsked() {
        return preferences.getBoolean(HAS_ASKED_FOR_CALENDAR_KEY, false);
    }

    // ===== CONTACTS ==============================================================================

    void setContactsPermissionsAsked() {
        preferences.edit()
                .putBoolean(HAS_ASKED_FOR_CONTACTS_KEY, true)
                .apply();
    }

    boolean isContactsPermissionsAsked() {
        return preferences.getBoolean(HAS_ASKED_FOR_CONTACTS_KEY, false);
    }

    // ===== PHONE ==============================================================================

    void setPhonePermissionsAsked() {
        preferences.edit()
                .putBoolean(HAS_ASKED_FOR_CALLING_KEY, true)
                .apply();
    }

    boolean isPhonePermissionsAsked() {
        return preferences.getBoolean(HAS_ASKED_FOR_CALLING_KEY, false);
    }

    // ===== STORAGE ===============================================================================

    void setStoragePermissionsAsked() {
        preferences.edit()
                .putBoolean(HAS_ASKED_FOR_STORAGE_KEY, true)
                .apply();
    }

    boolean isStoragePermissionsAsked() {
        return preferences.getBoolean(HAS_ASKED_FOR_STORAGE_KEY, false);
    }

    // ===== BODY SENSORS ==============================================================================

    void setBodySensorsPermissionsAsked() {
        preferences.edit()
                .putBoolean(HAS_ASKED_FOR_BODY_SENSORS_KEY, true)
                .apply();
    }

    boolean isBodySensorsPermissionsAsked() {
        return preferences.getBoolean(HAS_ASKED_FOR_BODY_SENSORS_KEY, false);
    }

    // ===== SMS ==============================================================================

    void setSmsPermissionsAsked() {
        preferences.edit()
                .putBoolean(HAS_ASKED_FOR_SMS_KEY, true)
                .apply();
    }

    boolean isSmsPermissionsAsked() {
        return preferences.getBoolean(HAS_ASKED_FOR_SMS_KEY, false);
    }

    // ===== TESTING ===============================================================================

    void clearData() {
        preferences.edit()
                .clear()
                .apply();
    }
}
