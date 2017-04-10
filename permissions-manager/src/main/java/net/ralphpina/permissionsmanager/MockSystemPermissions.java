package net.ralphpina.permissionsmanager;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.BODY_SENSORS;
import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.GET_ACCOUNTS;
import static android.Manifest.permission.READ_CALENDAR;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.SEND_SMS;
import static android.Manifest.permission.WRITE_CONTACTS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Used in testing to mock out the Android permissions. It is not meant to be used directly.
 */
public class MockSystemPermissions {

    // these will map to real permissions
    private final List<String>         mPermissionsGranted;
    private final List<String>         mPermissionsOkToGrant;
    private final List<String>         mPermissionsNeverAskAgain;
    private final Map<String, Integer> mPermissionRequestCount;

    protected MockSystemPermissions() {
        mPermissionsGranted = new ArrayList<>();
        mPermissionsOkToGrant = new ArrayList<>();
        mPermissionsNeverAskAgain = new ArrayList<>();
        mPermissionRequestCount = new HashMap<>();
    }

    boolean checkSelfPermission(@NonNull String permission) {
        return mPermissionsGranted.contains(permission);
    }

    public void neverAskAgain(@NonNull String... permissions) {
        Collections.addAll(mPermissionsNeverAskAgain, permissions);
    }

    boolean shouldShowRequestPermissionRationale(@NonNull String permission) {
        Integer permissionCount = mPermissionRequestCount.get(permission);
        return !mPermissionsNeverAskAgain.contains(permission) && permissionCount != null && permissionCount > 0;
    }

    void requestPermissions(String[] permissions) {
        for (String permission : permissions) {
            markPermissionRequest(permission);
            if (mPermissionsOkToGrant.contains(permission)) {
                mPermissionsGranted.add(permission);
            }
        }
    }

    private void markPermissionRequest(String permission) {
        if (mPermissionRequestCount.containsKey(permission)) {
            Integer value = mPermissionRequestCount.get(permission);
            if (value == null) {
                value = 0;
            }
            value++;
            mPermissionRequestCount.put(permission, value);
        } else {
            mPermissionRequestCount.put(permission, 1);
        }
    }

    // ---- CAMERA TEST HELPERS --------------------------------------------------------------------

    void setIsCameraGranted(boolean isCameraGranted) {
        if (isCameraGranted) {
            mPermissionsGranted.add(CAMERA);
        } else {
            mPermissionsGranted.remove(CAMERA);
        }
    }

    public void setShouldAllowCameraPermission(boolean shouldAllowCameraPermission) {
        if (shouldAllowCameraPermission) {
            mPermissionsOkToGrant.add(CAMERA);
        } else {
            mPermissionsOkToGrant.remove(CAMERA);
        }
    }

    // ---- AUDIO TEST HELPERS ---------------------------------------------------------------------

    void setIsAudioRecordingGranted(boolean isAudioRecordingGranted) {
        if (isAudioRecordingGranted) {
            mPermissionsGranted.add(RECORD_AUDIO);
        } else {
            mPermissionsGranted.remove(RECORD_AUDIO);
        }
    }

    public void setShouldAllowAudioRecordingPermission(boolean shouldAllowAudioRecordingPermission) {
        if (shouldAllowAudioRecordingPermission) {
            mPermissionsOkToGrant.add(RECORD_AUDIO);
        } else {
            mPermissionsOkToGrant.remove(RECORD_AUDIO);
        }
    }

    // ---- LOCATION TEST HELPERS ------------------------------------------------------------------

    void setIsLocationGranted(boolean isLocationGranted) {
        if (isLocationGranted) {
            mPermissionsGranted.add(ACCESS_FINE_LOCATION);
            mPermissionsGranted.add(ACCESS_COARSE_LOCATION);
        } else {
            mPermissionsGranted.remove(ACCESS_FINE_LOCATION);
            mPermissionsGranted.remove(ACCESS_COARSE_LOCATION);
        }
    }

    public void setShouldAllowLocationPermission(boolean shouldAllowLocationPermission) {
        if (shouldAllowLocationPermission) {
            mPermissionsOkToGrant.add(ACCESS_FINE_LOCATION);
            mPermissionsOkToGrant.add(ACCESS_COARSE_LOCATION);
        } else {
            mPermissionsOkToGrant.remove(ACCESS_FINE_LOCATION);
            mPermissionsOkToGrant.add(ACCESS_COARSE_LOCATION);
        }
    }

    // ---- CALENDAR TEST HELPERS ------------------------------------------------------------------

    void setIsCalendarGranted(boolean isLocationGranted) {
        if (isLocationGranted) {
            mPermissionsGranted.add(READ_CALENDAR);
        } else {
            mPermissionsGranted.remove(READ_CALENDAR);
        }
    }

    public void setShouldAllowCalendarPermission(boolean shouldAllowLocationPermission) {
        if (shouldAllowLocationPermission) {
            mPermissionsOkToGrant.add(READ_CALENDAR);
        } else {
            mPermissionsOkToGrant.remove(READ_CALENDAR);
        }
    }

    // ---- CONTACTS TEST HELPERS ------------------------------------------------------------------

    void setIsContactsGranted(boolean isContactsGranted) {
        if (isContactsGranted) {
            mPermissionsGranted.add(READ_CONTACTS);
            mPermissionsGranted.add(WRITE_CONTACTS);
            mPermissionsGranted.add(GET_ACCOUNTS);
        } else {
            mPermissionsGranted.remove(READ_CONTACTS);
            mPermissionsGranted.remove(WRITE_CONTACTS);
            mPermissionsGranted.remove(GET_ACCOUNTS);
        }
    }

    public void setShouldAllowContactsPermission(boolean shouldAllowLocationPermission) {
        if (shouldAllowLocationPermission) {
            mPermissionsOkToGrant.add(READ_CONTACTS);
            mPermissionsOkToGrant.add(WRITE_CONTACTS);
            mPermissionsOkToGrant.add(GET_ACCOUNTS);
        } else {
            mPermissionsOkToGrant.remove(READ_CONTACTS);
            mPermissionsOkToGrant.remove(WRITE_CONTACTS);
            mPermissionsOkToGrant.remove(GET_ACCOUNTS);
        }
    }

    // ---- CALLING TEST HELPERS -------------------------------------------------------------------

    void setIsCallingGranted(boolean isCallingGranted) {
        if (isCallingGranted) {
            mPermissionsGranted.add(CALL_PHONE);
        } else {
            mPermissionsGranted.remove(CALL_PHONE);
        }
    }

    public void setShouldAllowCallingPermission(boolean shouldAllowCallingPermission) {
        if (shouldAllowCallingPermission) {
            mPermissionsOkToGrant.add(CALL_PHONE);
        } else {
            mPermissionsOkToGrant.remove(CALL_PHONE);
        }
    }

    // ---- STORAGE TEST HELPERS ------------------------------------------------------------------

    void setIsStorageGranted(boolean isWriteStorageGranted) {
        if (isWriteStorageGranted) {
            mPermissionsGranted.add(WRITE_EXTERNAL_STORAGE);
            mPermissionsGranted.add(READ_EXTERNAL_STORAGE);
        } else {
            mPermissionsGranted.remove(WRITE_EXTERNAL_STORAGE);
            mPermissionsGranted.remove(READ_EXTERNAL_STORAGE);
        }
    }

    public void setShouldAllowStoragePermission(boolean shouldAllowWriteStoragePermission) {
        if (shouldAllowWriteStoragePermission) {
            mPermissionsOkToGrant.add(WRITE_EXTERNAL_STORAGE);
            mPermissionsOkToGrant.add(READ_EXTERNAL_STORAGE);
        } else {
            mPermissionsOkToGrant.remove(WRITE_EXTERNAL_STORAGE);
            mPermissionsOkToGrant.remove(READ_EXTERNAL_STORAGE);
        }
    }

    // ---- BODY SENSORS TEST HELPERS --------------------------------------------------------------

    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    void setIsBodySensorsGranted(boolean isBodySensorsGranted) {
        if (isBodySensorsGranted) {
            mPermissionsGranted.add(BODY_SENSORS);
        } else {
            mPermissionsGranted.remove(BODY_SENSORS);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    public void setShouldAllowBodySensorsPermission(boolean shouldAllowBodySensorsPermission) {
        if (shouldAllowBodySensorsPermission) {
            mPermissionsOkToGrant.add(BODY_SENSORS);
        } else {
            mPermissionsOkToGrant.remove(BODY_SENSORS);
        }
    }

    // ---- SMS TEST HELPERS -----------------------------------------------------------------------

    void setIsSmsGranted(boolean isSmsGranted) {
        if (isSmsGranted) {
            mPermissionsGranted.add(SEND_SMS);
        } else {
            mPermissionsGranted.remove(SEND_SMS);
        }
    }

    public void setShouldAllowSmsPermission(boolean shouldAllowSmsPermission) {
        if (shouldAllowSmsPermission) {
            mPermissionsOkToGrant.add(SEND_SMS);
        } else {
            mPermissionsOkToGrant.remove(SEND_SMS);
        }
    }
}
