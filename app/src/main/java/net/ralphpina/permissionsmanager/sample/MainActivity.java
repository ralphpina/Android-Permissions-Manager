package net.ralphpina.permissionsmanager.sample;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ScrollView;

import net.ralphpina.permissionsmanager.PermissionsManager;
import net.ralphpina.permissionsmanager.PermissionsResult;
import net.ralphpina.permissionsmanager.sample.databinding.MainActivityBinding;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;

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
        mPermissionsManagerStatus.updateStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    // ----- OnClick -----------------------------------------------------------------------

    @OnClick({
            R.id.button_go_to_permissions_camera,
            R.id.button_go_to_permissions_location,
            R.id.button_go_to_permissions_audio,
            R.id.button_go_to_permissions_calendar,
            R.id.button_go_to_permissions_contacts,
            R.id.button_go_to_permissions_calling,
            R.id.button_go_to_permissions_storage,
            R.id.button_go_to_permissions_body_sensor,
            R.id.button_go_to_permissions_sms
    })
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
                    .requestCameraPermission()
                    .subscribe(new Action1<PermissionsResult>() {
                        @Override
                        public void call(PermissionsResult permissionsResult) {
                            mPermissionsManagerStatus.updateStatus();
                            Snackbar.make(mRoot,
                                          permissionsResult.isGranted() ? "User granted camera." : "User denied camera.",
                                          LENGTH_SHORT)
                                    .show();
                        }
                    });
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
                    .requestLocationPermission()
                    .subscribe(new Action1<PermissionsResult>() {
                        @Override
                        public void call(PermissionsResult permissionsResult) {
                            mPermissionsManagerStatus.updateStatus();
                            Snackbar.make(mRoot,
                                          permissionsResult.isGranted() ? "User granted location." : "User denied location.",
                                          LENGTH_SHORT)
                                    .show();
                        }
                    });
        }
    }

    @OnClick(R.id.button_audio_permissions)
    public void onClickAskForAudio() {
        if (PermissionsManager.get()
                .isMicrophoneGranted()) {
            Snackbar.make(mRoot, R.string.audio_granted_dont_ask, LENGTH_SHORT)
                    .show();
        } else if (PermissionsManager.get()
                .neverAskForMicrophone(this)) {
            Snackbar.make(mRoot, R.string.user_selected_never_ask_again, LENGTH_SHORT)
                    .show();
        } else {
            PermissionsManager.get()
                    .requestMicrophonePermission()
                    .subscribe(new Action1<PermissionsResult>() {
                        @Override
                        public void call(PermissionsResult permissionsResult) {
                            mPermissionsManagerStatus.updateStatus();
                            Snackbar.make(mRoot,
                                          permissionsResult.isGranted() ? "User granted microphone." : "User denied microphone.",
                                          LENGTH_SHORT)
                                    .show();
                        }
                    });
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
                    .requestCalendarPermission()
                    .subscribe(new Action1<PermissionsResult>() {
                        @Override
                        public void call(PermissionsResult permissionsResult) {
                            mPermissionsManagerStatus.updateStatus();
                            Snackbar.make(mRoot,
                                          permissionsResult.isGranted() ? "User granted calendar." : "User denied calendar.",
                                          LENGTH_SHORT)
                                    .show();
                        }
                    });
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
                    .requestContactsPermission()
                    .subscribe(new Action1<PermissionsResult>() {
                        @Override
                        public void call(PermissionsResult permissionsResult) {
                            mPermissionsManagerStatus.updateStatus();
                            Snackbar.make(mRoot,
                                          permissionsResult.isGranted() ? "User granted contacts." : "User denied contacts.",
                                          LENGTH_SHORT)
                                    .show();
                        }
                    });
        }
    }

    @OnClick(R.id.button_calling_permissions)
    public void onClickAskForCalling() {
        if (PermissionsManager.get()
                .isPhoneGranted()) {
            Snackbar.make(mRoot, R.string.calling_granted_dont_ask, LENGTH_SHORT)
                    .show();
        } else if (PermissionsManager.get()
                .neverAskForPhone(this)) {
            Snackbar.make(mRoot, R.string.user_selected_never_ask_again, LENGTH_SHORT)
                    .show();
        } else {
            PermissionsManager.get()
                    .requestPhonePermission()
                    .subscribe(new Action1<PermissionsResult>() {
                        @Override
                        public void call(PermissionsResult permissionsResult) {
                            mPermissionsManagerStatus.updateStatus();
                            Snackbar.make(mRoot,
                                          permissionsResult.isGranted() ? "User granted phone." : "User denied phone.",
                                          LENGTH_SHORT)
                                    .show();
                        }
                    });
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
                    .requestStoragePermission()
                    .subscribe(new Action1<PermissionsResult>() {
                        @Override
                        public void call(PermissionsResult permissionsResult) {
                            mPermissionsManagerStatus.updateStatus();
                            Snackbar.make(mRoot,
                                          permissionsResult.isGranted() ? "User granted storage." : "User denied storage.",
                                          LENGTH_SHORT)
                                    .show();
                        }
                    });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @OnClick(R.id.button_body_sensor_permission)
    public void onClickAskForBodySensor() {
        if (PermissionsManager.get()
                .isBodySensorGranted()) {
            Snackbar.make(mRoot, R.string.body_sensor_granted_dont_ask, LENGTH_SHORT)
                    .show();
        } else if (PermissionsManager.get()
                .neverAskForBodySensor(this)) {
            Snackbar.make(mRoot, R.string.user_selected_never_ask_again, LENGTH_SHORT)
                    .show();
        } else {
            PermissionsManager.get()
                    .requestBodySensorPermission()
                    .subscribe(new Action1<PermissionsResult>() {
                        @Override
                        public void call(PermissionsResult permissionsResult) {
                            mPermissionsManagerStatus.updateStatus();
                            Snackbar.make(mRoot,
                                          permissionsResult.isGranted() ? "User granted body sensor." : "User denied body sensor.",
                                          LENGTH_SHORT)
                                    .show();
                        }
                    });
        }
    }

    @OnClick(R.id.button_sms_permissions)
    public void onClickAskForSms() {
        if (PermissionsManager.get()
                .isSmsGranted()) {
            Snackbar.make(mRoot, R.string.sms_granted_dont_ask, LENGTH_SHORT)
                    .show();
        } else if (PermissionsManager.get()
                .neverAskForSms(this)) {
            Snackbar.make(mRoot, R.string.user_selected_never_ask_again, LENGTH_SHORT)
                    .show();
        } else {
            PermissionsManager.get()
                    .requestSmsPermission()
                    .subscribe(new Action1<PermissionsResult>() {
                        @Override
                        public void call(PermissionsResult permissionsResult) {
                            mPermissionsManagerStatus.updateStatus();
                            Snackbar.make(mRoot,
                                          permissionsResult.isGranted() ? "User granted SMS." : "User denied SMS.",
                                          LENGTH_SHORT)
                                    .show();
                        }
                    });
        }
    }

    // ===== TESTING ===============================================================================

    @VisibleForTesting
    public PermissionsManagerStatus getPermissionsManagerStatus() {
        return mPermissionsManagerStatus;
    }
}
