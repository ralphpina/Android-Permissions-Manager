package net.ralphpina.permissionsmanager.sample;

import android.app.Application;

import net.ralphpina.permissionsmanager.PermissionsManager;

public class PMApplication extends Application {

    private static PMApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        PermissionsManager.init(this);
    }

    public static PMApplication get() {
        return mInstance;
    }
}
