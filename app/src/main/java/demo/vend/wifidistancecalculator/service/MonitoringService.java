package demo.vend.wifidistancecalculator.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import demo.vend.wifidistancecalculator.ApplicationState;
import demo.vend.wifidistancecalculator.R;
import demo.vend.wifidistancecalculator.utils.DialogUtils;

public class MonitoringService extends Service {

    private ConnectivityManager connectivityManager;
    private Timer timer;
    public static boolean isAlive;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                if (Objects.requireNonNull(wifi).isWifiEnabled()) {
                    fetchNetworks();
                }else {
                    onDestroy();
                }
            }
        }, 100, 3000);
        isAlive = true;
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        isAlive = false;
    }
    private List<ScanResult> listAllNetworks(WifiManager wifiManager) {
        wifiManager.startScan();
        return wifiManager.getScanResults();
    }
    private void fetchNetworks() {
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected() && ConnectivityManager.TYPE_WIFI == activeNetworkInfo.getType()) {
            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (wifiManager != null) {
                setNetworksInAppState(listAllNetworks(wifiManager));
            }
        } else {
            DialogUtils.showErrorDialog(this);
        }
    }

    synchronized private void setNetworksInAppState(List<ScanResult> scanResults) {
        ApplicationState.getInstance().setScanResults(scanResults);
    }
}
