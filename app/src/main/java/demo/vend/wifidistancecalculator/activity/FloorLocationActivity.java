package demo.vend.wifidistancecalculator.activity;

import android.net.wifi.ScanResult;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import demo.vend.wifidistancecalculator.ApplicationState;
import demo.vend.wifidistancecalculator.R;
import demo.vend.wifidistancecalculator.adapter.FloorListAdapter;
import demo.vend.wifidistancecalculator.adapter.NetworkListAdapter;
import demo.vend.wifidistancecalculator.callbacks.IItemClickListener;
import demo.vend.wifidistancecalculator.manager.DistanceCompartor;
import demo.vend.wifidistancecalculator.utils.Constants;

public class FloorLocationActivity extends BaseActivity implements IItemClickListener {
    List<ScanResult> scanResultList;
    HashMap<String, String> floorNamesMap;
    HashMap<String, String> regionNamesMap;
    RecyclerView recyclerView;
    FloorListAdapter floorListAdapter;
    private String[] floorNames;
    private  String[] regionNames;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor_location);
        init();
        initRecyclerView();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> updateStatus());
            }
        }, 0, 5000);
    }

    private void initRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(floorListAdapter);
    }

    private void init() {
        recyclerView = findViewById(R.id.recycler_view);
        floorNamesMap = ApplicationState.getInstance().getMacFilterNames();
        regionNamesMap = ApplicationState.getInstance().getFloorRegionNames();
        floorNames = getResources().getStringArray(R.array.floor_fields);
        regionNames = getResources().getStringArray(R.array.floor_regions);
        floorListAdapter = new FloorListAdapter(Arrays.asList(floorNames), floorNamesMap,regionNamesMap,this);
    }

    private void updateStatus() {
        scanResultList = ApplicationState.getInstance().getScanResults();
        if (scanResultList != null) {
            Collections.sort(scanResultList, new DistanceCompartor());
            floorListAdapter.updateData(scanResultList.get(0).BSSID);
         //   ((TextView) findViewById(R.id.tvInfo)).setText(getNameAndRegion());
        }// else
         //   ((TextView) findViewById(R.id.tvInfo)).setText(Constants.NOT_AVAILABLE_STRING);
    }

    private String getNameAndRegion() {
        String str = "You are on ";
        if (floorNamesMap != null) {
            if (floorNamesMap.containsKey(scanResultList.get(0).BSSID))
                str += floorNamesMap.get(scanResultList.get(0).BSSID);
            else
                str += "Floor N/A";
        }
        str += " & Region ";
        if (regionNamesMap != null) {
            if (regionNamesMap.containsKey(scanResultList.get(0).BSSID))
                str += regionNamesMap.get(scanResultList.get(0).BSSID);
            else
                str += "N/A";
        }
        return str;
    }

    @Override
    public void onItemClicked(Object object) {
        Toast.makeText(this,"Feature to be implemented",Toast.LENGTH_SHORT).show();
    }
}
