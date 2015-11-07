package net.ralphpina.permissionsmanager;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.GET_ACCOUNTS;
import static android.Manifest.permission.READ_CALENDAR;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
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

    protected boolean checkSelfPermission(@NonNull String permission) {
        return mPermissionsGranted.contains(permission);
    }

    public void neverAskAgain(@NonNull String... permissions) {
        Collections.addAll(mPermissionsNeverAskAgain, permissions);
    }

    protected void okToAskAgain(@NonNull String permission) {
        mPermissionsNeverAskAgain.remove(permission);
    }

    protected boolean shouldShowRequestPermissionRationale(@NonNull String permission) {
        Integer permissionCount = mPermissionRequestCount.get(permission);
        return !mPermissionsNeverAskAgain.contains(permission) && permissionCount != null && permissionCount > 0;
    }

    public void requestPermissions(String[] permissions) {
        for (String permission : permissions) {
            markPermissionRequest(permission);
            if (mPermissionsOkToGrant.contains(permission)) {
                mPermissionsGranted.add(permission);
            }
        }
    }

    public void requestPermissions(Activity activity, String[] permissions, int requestCode) {
        requestPermissions(permissions);
        final int[] grantResults = getPermissionsGranted(permissions);
    }

    public void markPermissionRequest(String permission) {
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

    private int[] getPermissionsGranted(@NonNull String[] permissions) {
        final int[] grantResults = new int[permissions.length];
        for (int i = 0; i < permissions.length; i++) {
            if (checkSelfPermission(permissions[i])) {
                grantResults[i] = PackageManager.PERMISSION_GRANTED;
            } else {
                grantResults[i] = PackageManager.PERMISSION_DENIED;
            }
        }
        return grantResults;
    }

    // ---- CAMERA TEST HELPERS --------------------------------------------------------------------

    public void setIsCameraGranted(boolean isCameraGranted) {
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

    public void setIsAudioRecordingGranted(boolean isAudioRecordingGranted) {
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

    public void setIsLocationGranted(boolean isLocationGranted) {
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

    public void setIsCalendarGranted(boolean isLocationGranted) {
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

    public void setIsContactsGranted(boolean isContactsGranted) {
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

    public void setIsCallingGranted(boolean isCallingGranted) {
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

    public void setIsStorageGranted(boolean isWriteStorageGranted) {
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

    public void setPermissionsGranted(String permissionsGranted) {
        mPermissionsGranted.add(permissionsGranted);
    }
}
