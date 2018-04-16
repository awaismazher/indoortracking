package demo.vend.wifidistancecalculator.activity;

import android.net.wifi.ScanResult;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import demo.vend.wifidistancecalculator.ApplicationState;
import demo.vend.wifidistancecalculator.R;
import demo.vend.wifidistancecalculator.manager.DistanceCompartor;
import demo.vend.wifidistancecalculator.utils.Constants;

public class FloorLocationActivity extends BaseActivity {
    List<ScanResult> scanResultList;
    HashMap<String, String> floorNamesMap;
    HashMap<String, String> regionNamesMap;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor_location);
       floorNamesMap = ApplicationState.getInstance().getMacFilterNames();
       regionNamesMap = ApplicationState.getInstance().getFloorRegionNames();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> updateStatus());
            }
        }, 0, 5000);
    }

    private void updateStatus() {
        scanResultList = ApplicationState.getInstance().getScanResults();
        if(scanResultList!=null) {
            Collections.sort(scanResultList, new DistanceCompartor());
            ((TextView) findViewById(R.id.tvInfo)).setText(getNameAndRegion());
        }else
            ((TextView) findViewById(R.id.tvInfo)).setText(Constants.NOT_AVAILABLE_STRING);
    }

    private String getNameAndRegion() {
        String str = "You are on ";
        if(floorNamesMap!=null){
            if(floorNamesMap.containsKey(scanResultList.get(0).BSSID))
                str+=floorNamesMap.get(scanResultList.get(0).BSSID);
            else
                str += "Floor N/A";
        }
        str+=" & Region ";
        if(regionNamesMap!=null){
            if(regionNamesMap.containsKey(scanResultList.get(0).BSSID))
                str+=regionNamesMap.get(scanResultList.get(0).BSSID);
            else
                str += "N/A";
        }
        return str;
    }
}
