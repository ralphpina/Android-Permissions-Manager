package net.ralphpina.permissionsmanager;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

@TargetApi(Build.VERSION_CODES.M)
public class PermissionRequestActivity extends AppCompatActivity {
    private static final String PERMISSIONS_KEY     = "permissions";
    private static final int    PERMISSIONS_REQUEST = 420;

    private PermissionsManager permissionsManager;

    private static boolean isAskingForPermissions = false;

    /* PACKAGE */ static Intent getIntent(Context context, String... permissions) {
        Intent i = new Intent(context, PermissionRequestActivity.class);
        i.putExtra(PERMISSIONS_KEY, permissions);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissionsManager = PermissionsManager.get();
        if (savedInstanceState == null) {
            handleIntent(getIntent());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        isAskingForPermissions = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isAskingForPermissions = false;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        String[] permissions = intent.getStringArrayExtra(PERMISSIONS_KEY);
        requestPermissions(permissions, PERMISSIONS_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if(requestCode != PERMISSIONS_REQUEST) {
            return;
        }
        permissionsManager.onRequestPermissionsResult(permissions, grantResults);
        finish();
    }


    /* PACKAGE */ static boolean isAskingForPermissions() {
        return isAskingForPermissions;
    }
}
