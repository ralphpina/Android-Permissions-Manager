package net.ralphpina.permissionsmanager;

import android.content.Context;

import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

/**
 * Wraps SnappyDb and provides readable methods
 */
public class DbHelper {

    private static final String DB_NAME = "android_permissions_manager";

    private static final String HAS_ASKED_FOR_CAMERA_KEY          = "has_asked_for_camera";
    private static final String HAS_ASKED_FOR_LOCATION_KEY        = "has_asked_for_location";
    private static final String HAS_ASKED_FOR_AUDIO_RECORDING_KEY = "has_asked_for_audio_recording";
    private static final String HAS_ASKED_FOR_CALENDAR_KEY        = "has_asked_for_calendar";
    private static final String HAS_ASKED_FOR_CONTACTS_KEY        = "has_asked_for_contacts";
    private static final String HAS_ASKED_FOR_CALLING_KEY         = "has_asked_for_calling";
    private static final String HAS_ASKED_FOR_STORAGE_KEY         = "has_asked_for_storage";

    private static DbHelper mInstance;
    private        DB       mDb;

    protected static void init(Context context) throws SnappydbException {
        mInstance = new DbHelper(context);
    }

    protected void tearDown() {
        try {
            mDb.destroy();
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
        mInstance = null;
    }

    private DbHelper(Context context) throws SnappydbException {
        mDb = DBFactory.open(context, DB_NAME);
    }

    protected static DbHelper get() {
        return mInstance;
    }

    // ==== CAMERA =================================================================================

    public void setCameraPermissionsAsked() {
        try {
            mDb.putBoolean(HAS_ASKED_FOR_CAMERA_KEY, true);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

    public boolean isCameraPermissionsAsked() {
        try {
            return mDb.getBoolean(HAS_ASKED_FOR_CAMERA_KEY);
        } catch (SnappydbException e) {
            return false;
        }
    }

    // ===== LOCATION ==============================================================================

    public void setLocationPermissionsAsked() {
        try {
            mDb.putBoolean(HAS_ASKED_FOR_LOCATION_KEY, true);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

    public boolean isLocationPermissionsAsked() {
        try {
            return mDb.getBoolean(HAS_ASKED_FOR_LOCATION_KEY);
        } catch (SnappydbException e) {
            return false;
        }
    }

    // ===== AUDIO RECORDING =======================================================================

    public void setAudioPermissionsAsked() {
        try {
            mDb.putBoolean(HAS_ASKED_FOR_AUDIO_RECORDING_KEY, true);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

    public boolean isAudioPermissionsAsked() {
        try {
            return mDb.getBoolean(HAS_ASKED_FOR_AUDIO_RECORDING_KEY);
        } catch (SnappydbException e) {
            return false;
        }
    }

    // ===== CALENDAR ==============================================================================

    public void setCalendarPermissionsAsked() {
        try {
            mDb.putBoolean(HAS_ASKED_FOR_CALENDAR_KEY, true);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

    public boolean isCalendarPermissionsAsked() {
        try {
            return mDb.getBoolean(HAS_ASKED_FOR_CALENDAR_KEY);
        } catch (SnappydbException e) {
            return false;
        }
    }

    // ===== CONTACTS ==============================================================================

    public void setContactsPermissionsAsked() {
        try {
            mDb.putBoolean(HAS_ASKED_FOR_CONTACTS_KEY, true);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

    public boolean isContactsPermissionsAsked() {
        try {
            return mDb.getBoolean(HAS_ASKED_FOR_CONTACTS_KEY);
        } catch (SnappydbException e) {
            return false;
        }
    }

    // ===== CALLING ==============================================================================

    public void setCallingPermissionsAsked() {
        try {
            mDb.putBoolean(HAS_ASKED_FOR_CALLING_KEY, true);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

    public boolean isCallingPermissionsAsked() {
        try {
            return mDb.getBoolean(HAS_ASKED_FOR_CALLING_KEY);
        } catch (SnappydbException e) {
            return false;
        }
    }

    // ===== STORAGE ===============================================================================

    public void setStoragePermissionsAsked() {
        try {
            mDb.putBoolean(HAS_ASKED_FOR_STORAGE_KEY, true);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

    public boolean isStoragePermissionsAsked() {
        try {
            return mDb.getBoolean(HAS_ASKED_FOR_STORAGE_KEY);
        } catch (SnappydbException e) {
            return false;
        }
    }

    // ===== TESTING ===============================================================================

    public void clearData() {
        try {
            mDb.putBoolean(HAS_ASKED_FOR_CAMERA_KEY, false);
            mDb.putBoolean(HAS_ASKED_FOR_LOCATION_KEY, false);
            mDb.putBoolean(HAS_ASKED_FOR_AUDIO_RECORDING_KEY, false);
            mDb.putBoolean(HAS_ASKED_FOR_CALENDAR_KEY, false);
            mDb.putBoolean(HAS_ASKED_FOR_CONTACTS_KEY, false);
            mDb.putBoolean(HAS_ASKED_FOR_CALLING_KEY, false);
            mDb.putBoolean(HAS_ASKED_FOR_STORAGE_KEY, false);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }
}
