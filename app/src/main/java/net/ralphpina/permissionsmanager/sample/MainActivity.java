package net.ralphpina.permissionsmanager.sample;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ScrollView;

import net.ralphpina.permissionsmanager.PermissionsManager;
import net.ralphpina.permissionsmanager.sample.databinding.MainActivityBinding;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.support.design.widget.Snackbar.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar    mToolbar;
    @Bind(R.id.root)
    ScrollView mRoot;

    private PermissionsManagerStatus mPermissionsManagerStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.main_activity);
        ButterKnife.bind(this);
        mPermissionsManagerStatus = new PermissionsManagerStatus(this);
        binding.setPermissions(mPermissionsManagerStatus);
        setSupportActionBar(mToolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPermissionsManagerStatus.updateStatus();
    }

    // ----- OnClick -----------------------------------------------------------------------

    @OnClick({R.id.button_go_to_permissions_camera,
              R.id.button_go_to_permissions_location,
              R.id.button_go_to_permissions_audio,
              R.id.button_go_to_permissions_calendar,
              R.id.button_go_to_permissions_contacts,
              R.id.button_go_to_permissions_calling,
              R.id.button_go_to_permissions_storage})
    public void onClickGoToAppSettings() {
        PermissionsManager.get()
                          .intentToAppSettings(this);
    }

    @OnClick(R.id.button_camera_permissions)
    public void onClickAskForCamera() {
        if (PermissionsManager.get()
                              .isCameraGranted()) {
            Snackbar.make(mRoot, R.string.camera_granted_dont_ask, LENGTH_SHORT)
                    .show();
        } else if (PermissionsManager.get()
                                     .neverAskForCamera(this)) {
            Snackbar.make(mRoot, R.string.user_selected_never_ask_again, LENGTH_SHORT)
                    .show();
        } else {
            PermissionsManager.get()
                              .requestCameraPermission(this);
        }
    }

    @OnClick(R.id.button_location_permissions)
    public void onClickAskForLocation() {
        if (PermissionsManager.get()
                              .isLocationGranted()) {
            Snackbar.make(mRoot, R.string.location_granted_dont_ask, LENGTH_SHORT)
                    .show();
        } else if (PermissionsManager.get()
                                     .neverAskForLocation(this)) {
            Snackbar.make(mRoot, R.string.user_selected_never_ask_again, LENGTH_SHORT)
                    .show();
        } else {
            PermissionsManager.get()
                              .requestLocationPermission(this);
        }
    }

    @OnClick(R.id.button_audio_permissions)
    public void onClickAskForAudio() {
        if (PermissionsManager.get()
                              .isAudioRecordingGranted()) {
            Snackbar.make(mRoot, R.string.audio_granted_dont_ask, LENGTH_SHORT)
                    .show();
        } else if (PermissionsManager.get()
                                     .neverAskForAudio(this)) {
            Snackbar.make(mRoot, R.string.user_selected_never_ask_again, LENGTH_SHORT)
                    .show();
        } else {
            PermissionsManager.get()
                              .requestAudioRecordingPermission(this);
        }
    }

    @OnClick(R.id.button_calendar_permissions)
    public void onClickAskForCalendar() {
        if (PermissionsManager.get()
                              .isCalendarGranted()) {
            Snackbar.make(mRoot, R.string.calendar_granted_dont_ask, LENGTH_SHORT)
                    .show();
        } else if (PermissionsManager.get()
                                     .neverAskForCalendar(this)) {
            Snackbar.make(mRoot, R.string.user_selected_never_ask_again, LENGTH_SHORT)
                    .show();
        } else {
            PermissionsManager.get()
                              .requestCalendarPermission(this);
        }
    }

    @OnClick(R.id.button_contacts_permissions)
    public void onClickAskForContacts() {
        if (PermissionsManager.get()
                              .isContactsGranted()) {
            Snackbar.make(mRoot, R.string.contacts_granted_dont_ask, LENGTH_SHORT)
                    .show();
        } else if (PermissionsManager.get()
                                     .neverAskForContacts(this)) {
            Snackbar.make(mRoot, R.string.user_selected_never_ask_again, LENGTH_SHORT)
                    .show();
        } else {
            PermissionsManager.get()
                              .requestContactsPermission(this);
        }
    }

    @OnClick(R.id.button_calling_permissions)
    public void onClickAskForCalling() {
        if (PermissionsManager.get()
                              .isCallingGranted()) {
            Snackbar.make(mRoot, R.string.calling_granted_dont_ask, LENGTH_SHORT)
                    .show();
        } else if (PermissionsManager.get()
                                     .neverAskForCalling(this)) {
            Snackbar.make(mRoot, R.string.user_selected_never_ask_again, LENGTH_SHORT)
                    .show();
        } else {
            PermissionsManager.get()
                              .requestCallingPermission(this);
        }
    }

    @OnClick(R.id.button_storage_permissions)
    public void onClickAskForStorage() {
        if (PermissionsManager.get()
                              .isStorageGranted()) {
            Snackbar.make(mRoot, R.string.storage_granted_dont_ask, LENGTH_SHORT)
                    .show();
        } else if (PermissionsManager.get()
                                     .neverAskForStorage(this)) {
            Snackbar.make(mRoot, R.string.user_selected_never_ask_again, LENGTH_SHORT)
                    .show();
        } else {
            PermissionsManager.get()
                              .requestStoragePermission(this);
        }
    }

    // ===== TESTING ===============================================================================

    @VisibleForTesting
    public PermissionsManagerStatus getPermissionsManagerStatus() {
        return mPermissionsManagerStatus;
    }
}
