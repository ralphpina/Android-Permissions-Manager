package net.ralphpina.permissionsmanager.sample;

import android.app.Activity;
import android.databinding.BaseObservable;

import net.ralphpina.permissionsmanager.PermissionsManager;

/**
 * This class simply wraps {@link PermissionsManager} to use Android's Data Bindings
 */
public class PermissionsManagerStatus extends BaseObservable {

    private static final String TAG = "PermissionManagerStatus";

    private final Activity mActivity;

    // Camera
    private       String   cameraPermissionStatus;
    private       String   cameraHasAsked;
    private       boolean  cameraNeverAskAgain;

    // Location
    private       String   locationPermissionStatus;
    private       String   locationHasAsked;
    private       boolean  locationNeverAskAgain;

    // Audio
    private       String   audioPermissionStatus;
    private       String   audioHasAsked;
    private       boolean  audioNeverAskAgain;

    // Calendar
    private       String   calendarPermissionStatus;
    private       String   calendarHasAsked;
    private       boolean  calendarNeverAskAgain;

    // Contacts
    private       String   contactsPermissionStatus;
    private       String   contactsHasAsked;
    private       boolean  contactsNeverAskAgain;

    // Calling
    private       String   callingPermissionStatus;
    private       String   callingHasAsked;
    private       boolean  callingNeverAskAgain;

    // Storage
    private       String   storagePermissionStatus;
    private       String   storageHasAsked;
    private       boolean  storageNeverAskAgain;

    public PermissionsManagerStatus(Activity activity) {
        mActivity = activity;
    }

    public void updateStatus() {
        updateCameraStatus();
        updateLocationStatus();
        updateAudioStatus();
        updateCalendarStatus();
        updateContactsStatus();
        updateCallingStatus();
        updateStorageStatus();
        notifyChange();
    }

    // ----- Camera Status -------------------------------------------------------------------------

    private void updateCameraStatus() {
        this.cameraPermissionStatus = PMApplication.get()
                                                   .getString(PermissionsManager.get()
                                                                                .isCameraGranted()
                                                              ? R.string.given
                                                              : R.string.not_given);
        this.cameraHasAsked = PMApplication.get()
                                           .getString(PermissionsManager.get()
                                                                        .hasAskedForCameraPermission()
                                                      ? R.string.yes
                                                      : R.string.no);
        this.cameraNeverAskAgain = PermissionsManager.get()
                                                     .neverAskForCamera(mActivity);
    }

    // ----- Location Status -----------------------------------------------------------------------

    private void updateLocationStatus() {
        this.locationPermissionStatus = PMApplication.get()
                                                   .getString(PermissionsManager.get()
                                                                                .isLocationGranted()
                                                              ? R.string.given
                                                              : R.string.not_given);
        this.locationHasAsked = PMApplication.get()
                                           .getString(PermissionsManager.get()
                                                                        .hasAskedForLocationPermission()
                                                      ? R.string.yes
                                                      : R.string.no);
        this.locationNeverAskAgain = PermissionsManager.get()
                                                     .neverAskForLocation(mActivity);
    }

    // ----- Audio Status --------------------------------------------------------------------------

    private void updateAudioStatus() {
        this.audioPermissionStatus = PMApplication.get()
                                                   .getString(PermissionsManager.get()
                                                                                .isAudioRecordingGranted()
                                                              ? R.string.given
                                                              : R.string.not_given);
        this.audioHasAsked = PMApplication.get()
                                           .getString(PermissionsManager.get()
                                                                        .hasAskedForAudioRecordingPermission()
                                                      ? R.string.yes
                                                      : R.string.no);
        this.audioNeverAskAgain = PermissionsManager.get()
                                                     .neverAskForAudio(mActivity);
    }

    // ----- Calendar Status -----------------------------------------------------------------------

    private void updateCalendarStatus() {
        this.calendarPermissionStatus = PMApplication.get()
                                                   .getString(PermissionsManager.get()
                                                                                .isCalendarGranted()
                                                              ? R.string.given
                                                              : R.string.not_given);
        this.calendarHasAsked = PMApplication.get()
                                           .getString(PermissionsManager.get()
                                                                        .hasAskedForCalendarPermission()
                                                      ? R.string.yes
                                                      : R.string.no);
        this.calendarNeverAskAgain = PermissionsManager.get()
                                                     .neverAskForCalendar(mActivity);
    }

    // ----- Contact Status ------------------------------------------------------------------------

    private void updateContactsStatus() {
        this.contactsPermissionStatus = PMApplication.get()
                                                   .getString(PermissionsManager.get()
                                                                                .isContactsGranted()
                                                              ? R.string.given
                                                              : R.string.not_given);
        this.contactsHasAsked = PMApplication.get()
                                           .getString(PermissionsManager.get()
                                                                        .hasAskedForContactsPermission()
                                                      ? R.string.yes
                                                      : R.string.no);
        this.contactsNeverAskAgain = PermissionsManager.get()
                                                     .neverAskForContacts(mActivity);
    }

    // ----- Calling Status -------------------------------------------------------------------------

    private void updateCallingStatus() {
        this.callingPermissionStatus = PMApplication.get()
                                                   .getString(PermissionsManager.get()
                                                                                .isCallingGranted()
                                                              ? R.string.given
                                                              : R.string.not_given);
        this.callingHasAsked = PMApplication.get()
                                           .getString(PermissionsManager.get()
                                                                        .hasAskedForCallingPermission()
                                                      ? R.string.yes
                                                      : R.string.no);
        this.callingNeverAskAgain = PermissionsManager.get()
                                                     .neverAskForCalling(mActivity);
    }

    // ----- Storage Status ------------------------------------------------------------------------

    private void updateStorageStatus() {
        this.storagePermissionStatus = PMApplication.get()
                                                   .getString(PermissionsManager.get()
                                                                                .isStorageGranted()
                                                              ? R.string.given
                                                              : R.string.not_given);
        this.storageHasAsked = PMApplication.get()
                                           .getString(PermissionsManager.get()
                                                                        .hasAskedForStoragePermission()
                                                      ? R.string.yes
                                                      : R.string.no);
        this.storageNeverAskAgain = PermissionsManager.get()
                                                     .neverAskForStorage(mActivity);
    }

    // ===== CAMERA ================================================================================

    // ----- Permission Status ---------------------------------------------------------------------

    @DataBinding
    public String getCameraPermissionStatus() {
        return cameraPermissionStatus;
    }

    @DataBinding
    public void setCameraPermissionStatus(String cameraPermissionStatus) {
        this.cameraPermissionStatus = cameraPermissionStatus;
    }

    // ----- Has Asked -----------------------------------------------------------------------------

    @DataBinding
    public String getCameraHasAsked() {
        return cameraHasAsked;
    }

    @DataBinding
    public void setCameraHasAsked(String cameraHasAsked) {
        this.cameraHasAsked = cameraHasAsked;
    }

    // ----- Never Ask Again -----------------------------------------------------------------------

    @DataBinding
    public boolean isCameraNeverAskAgain() {
        return cameraNeverAskAgain;
    }

    @DataBinding
    public void setCameraNeverAskAgain(boolean cameraNeverAskAgain) {
        this.cameraNeverAskAgain = cameraNeverAskAgain;
    }

    // ===== LOCATION ==============================================================================

    // ----- Permission Status ---------------------------------------------------------------------

    public String getLocationPermissionStatus() {
        return locationPermissionStatus;
    }

    public void setLocationPermissionStatus(String locationPermissionStatus) {
        this.locationPermissionStatus = locationPermissionStatus;
    }

    // ----- Has Asked -----------------------------------------------------------------------------

    public String getLocationHasAsked() {
        return locationHasAsked;
    }

    public void setLocationHasAsked(String locationHasAsked) {
        this.locationHasAsked = locationHasAsked;
    }

    // ----- Never Ask Again -----------------------------------------------------------------------

    public boolean isLocationNeverAskAgain() {
        return locationNeverAskAgain;
    }

    public void setLocationNeverAskAgain(boolean locationNeverAskAgain) {
        this.locationNeverAskAgain = locationNeverAskAgain;
    }

    // ===== AUDIO =================================================================================

    // ----- Permission Status ---------------------------------------------------------------------

    public String getAudioPermissionStatus() {
        return audioPermissionStatus;
    }

    public void setAudioPermissionStatus(String audioPermissionStatus) {
        this.audioPermissionStatus = audioPermissionStatus;
    }

    // ----- Has Asked -----------------------------------------------------------------------------

    public String getAudioHasAsked() {
        return audioHasAsked;
    }

    public void setAudioHasAsked(String audioHasAsked) {
        this.audioHasAsked = audioHasAsked;
    }

    // ----- Never Ask Again -----------------------------------------------------------------------

    public boolean isAudioNeverAskAgain() {
        return audioNeverAskAgain;
    }

    public void setAudioNeverAskAgain(boolean audioNeverAskAgain) {
        this.audioNeverAskAgain = audioNeverAskAgain;
    }

    // ===== CALENDAR ==============================================================================

    // ----- Permission Status ---------------------------------------------------------------------

    public String getCalendarPermissionStatus() {
        return calendarPermissionStatus;
    }

    public void setCalendarPermissionStatus(String calendarPermissionStatus) {
        this.calendarPermissionStatus = calendarPermissionStatus;
    }

    // ----- Has Asked -----------------------------------------------------------------------------

    public String getCalendarHasAsked() {
        return calendarHasAsked;
    }

    public void setCalendarHasAsked(String calendarHasAsked) {
        this.calendarHasAsked = calendarHasAsked;
    }

    // ----- Never Ask Again -----------------------------------------------------------------------

    public boolean isCalendarNeverAskAgain() {
        return calendarNeverAskAgain;
    }

    public void setCalendarNeverAskAgain(boolean calendarNeverAskAgain) {
        this.calendarNeverAskAgain = calendarNeverAskAgain;
    }

    // ===== CONTACTS ==============================================================================

    // ----- Permission Status ---------------------------------------------------------------------

    public String getContactsPermissionStatus() {
        return contactsPermissionStatus;
    }

    public void setContactsPermissionStatus(String contactsPermissionStatus) {
        this.contactsPermissionStatus = contactsPermissionStatus;
    }

    // ----- Has Asked -----------------------------------------------------------------------------

    public String getContactsHasAsked() {
        return contactsHasAsked;
    }

    public void setContactsHasAsked(String contactsHasAsked) {
        this.contactsHasAsked = contactsHasAsked;
    }

    // ----- Never Ask Again -----------------------------------------------------------------------

    public boolean isContactsNeverAskAgain() {
        return contactsNeverAskAgain;
    }

    public void setContactsNeverAskAgain(boolean contactsNeverAskAgain) {
        this.contactsNeverAskAgain = contactsNeverAskAgain;
    }

    // ===== CALLING ===============================================================================

    // ----- Permission Status ---------------------------------------------------------------------

    public String getCallingPermissionStatus() {
        return callingPermissionStatus;
    }

    public void setCallingPermissionStatus(String callingPermissionStatus) {
        this.callingPermissionStatus = callingPermissionStatus;
    }

    // ----- Has Asked -----------------------------------------------------------------------------

    public String getCallingHasAsked() {
        return callingHasAsked;
    }

    public void setCallingHasAsked(String callingHasAsked) {
        this.callingHasAsked = callingHasAsked;
    }

    // ----- Never Ask Again -----------------------------------------------------------------------

    public boolean isCallingNeverAskAgain() {
        return callingNeverAskAgain;
    }

    public void setCallingNeverAskAgain(boolean callingNeverAskAgain) {
        this.callingNeverAskAgain = callingNeverAskAgain;
    }

    // ===== STORAGE ===============================================================================

    // ----- Permission Status ---------------------------------------------------------------------

    public String getStoragePermissionStatus() {
        return storagePermissionStatus;
    }

    public void setStoragePermissionStatus(String storagePermissionStatus) {
        this.storagePermissionStatus = storagePermissionStatus;
    }

    // ----- Has Asked -----------------------------------------------------------------------------

    public String getStorageHasAsked() {
        return storageHasAsked;
    }

    public void setStorageHasAsked(String storageHasAsked) {
        this.storageHasAsked = storageHasAsked;
    }

    // ----- Never Ask Again -----------------------------------------------------------------------

    public boolean isStorageNeverAskAgain() {
        return storageNeverAskAgain;
    }

    public void setStorageNeverAskAgain(boolean storageNeverAskAgain) {
        this.storageNeverAskAgain = storageNeverAskAgain;
    }
}
