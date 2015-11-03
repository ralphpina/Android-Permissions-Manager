package net.ralphpina.permissionsmanager.sample;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import net.ralphpina.permissionsmanager.sample.databinding.MainActivityBinding;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private PermissionManagerStatus mPermissionManagerStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.main_activity);
        mPermissionManagerStatus = new PermissionManagerStatus(this);
        binding.setPermissions(mPermissionManagerStatus);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPermissionManagerStatus.updateStatus();
    }
}
