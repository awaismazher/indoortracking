package demo.vend.wifidistancecalculator.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;

import java.util.Objects;

import demo.vend.wifidistancecalculator.ApplicationState;
import demo.vend.wifidistancecalculator.R;
import demo.vend.wifidistancecalculator.service.MonitoringService;
import demo.vend.wifidistancecalculator.storage.AppPreference;
import demo.vend.wifidistancecalculator.utils.Constants;
import demo.vend.wifidistancecalculator.utils.DialogUtils;

public class DashboardActivity extends BaseActivity implements View.OnClickListener{

    private Intent serviceIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        populateUserDetails();
        ApplicationState.getInstance().setMacFilterNames(AppPreference.
                getHashMap(getApplicationContext(), Constants.KEY_MAC_ADDRESS));
        ApplicationState.getInstance().setFloorRegionNames(AppPreference.
                getHashMap(getApplicationContext(), Constants.KEY_FLOOR_REGIONS));

    }

    private void populateUserDetails() {
        String username = AppPreference.getString(mContext, Constants.KEY_USER_NAME,Constants.EMPTY_STRING);
        if(!Objects.requireNonNull(username).isEmpty())
            ((TextView)findViewById(R.id.tv_welcome)).setText(getString(R.string.welcome_1,username));
        else
            findViewById(R.id.tv_welcome).setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (Objects.requireNonNull(wifi).isWifiEnabled()){
            serviceIntent = new Intent(this, MonitoringService.class);
            startService(serviceIntent);
        }else {
            DialogUtils.showDialog(DashboardActivity.this, getString(R.string.turn_wifi_on),
                    Constants.EMPTY_STRING, (dialog, which) -> startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS)));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_admin:
                Intent intent = new Intent(this, AdminActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_locate:
                intent = new Intent(this, FloorLocationActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(serviceIntent);
    }
}
