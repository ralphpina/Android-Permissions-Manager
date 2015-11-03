package net.ralphpina.permissionsmanager.sample;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.support.design.widget.Snackbar;
import android.view.View;

import net.ralphpina.permissionsmanager.PermissionsManager;

import static android.support.design.widget.Snackbar.LENGTH_SHORT;

/**
 * This class simply wraps {@link PermissionsManager} to use Android's Data Bindings
 */
public class PermissionManagerStatus extends BaseObservable {

    private static final String TAG = "PermissionManagerStatus";

    private       String   cameraPermissionStatus;
    private       String   cameraHasAsked;
    private       boolean  cameraNeverAskAgain;
    private final Activity mActivity;

    public PermissionManagerStatus(Activity activity) {
        mActivity = activity;
    }

    public void updateStatus() {
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
        notifyChange();
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

    // ----- OnClick -----------------------------------------------------------------------

    @DataBinding
    public void onClickAskForCamera(View view) {
        if (PermissionsManager.get().isCameraGranted()) {
            Snackbar.make(view, mActivity.getString(R.string.camera_granted_dont_ask), LENGTH_SHORT).show();
        } else if (PermissionsManager.get().neverAskForCamera(mActivity)) {
            Snackbar.make(view, R.string.user_selected_never_ask_again, LENGTH_SHORT).show();
        } else {
            PermissionsManager.get()
                              .requestCameraPermission(mActivity);
        }
    }
}
