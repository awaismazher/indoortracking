package demo.vend.wifidistancecalculator.activity;

import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;

import demo.vend.wifidistancecalculator.ApplicationState;
import demo.vend.wifidistancecalculator.fragments.InfoDialogFragment;
import demo.vend.wifidistancecalculator.manager.NavigationManager;
import demo.vend.wifidistancecalculator.service.MonitoringService;
import demo.vend.wifidistancecalculator.utils.Constants;
import demo.vend.wifidistancecalculator.callbacks.INetworkClickListener;
import demo.vend.wifidistancecalculator.adapter.NetworkListAdapter;
import demo.vend.wifidistancecalculator.R;
import demo.vend.wifidistancecalculator.storage.AppPreference;
import demo.vend.wifidistancecalculator.utils.DialogUtils;

public class AdminActivity extends BaseActivity implements View.OnClickListener, INetworkClickListener {

    TextView tvStringOfNetworks;
    ScheduledExecutorService ses;
    RecyclerView recyclerView;
    NetworkListAdapter networkListAdapter;
    private Timer timer;
    private Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        ApplicationState.getInstance().setMacFilterNames(AppPreference.getHashMap(this, Constants.KEY_MAC_ADDRESS));
        tvStringOfNetworks = findViewById(R.id.tvList);
        recyclerView = findViewById(R.id.recycler_view);
        startMonitoringService();
        DialogUtils.showWaitProgressDialog(AdminActivity.this);

    }

    private void startMonitoringService() {
        if(!MonitoringService.isAlive) {
            serviceIntent = new Intent(this, MonitoringService.class);
            startService(serviceIntent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> fetchNetworks());
            }
        }, 1000, 5000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
    }

    private void fetchNetworks() {
        List<ScanResult> scanResults = ApplicationState.getInstance().getScanResults();
        if(scanResults!=null) {
            initAdapter(scanResults);
        }else {
            DialogUtils.hideProgressDialog();
            DialogUtils.showErrorDialog(this);
            timer.cancel();
        }
    }

    private void initAdapter(List<ScanResult> scanResults) {
        DialogUtils.hideProgressDialog();
        if (scanResults.isEmpty()) {
            updateNoContentViewVisibility();
        } else {
            networkListAdapter = new NetworkListAdapter(scanResults, this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(networkListAdapter);
            updateNoContentViewVisibility();
        }
    }

    private void updateNoContentViewVisibility() {
        TextView tvList = findViewById(R.id.tvList);
        if (tvList.getVisibility() == View.GONE)
            tvList.setVisibility(View.VISIBLE);
        else
            tvList.setVisibility(View.GONE);
    }



    private int convertFrequencyToChannel(int freq) {
        if (freq >= 2412 && freq <= 2484) {
            return (freq - 2412) / 5 + 1;
        } else if (freq >= 5170 && freq <= 5825) {
            return (freq - 5170) / 5 + 34;
        } else {
            return -1;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_locate_yourself:
                Intent intent = new Intent(this, FloorLocationActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppPreference.saveHashMap(this, Constants.KEY_MAC_ADDRESS, ApplicationState.getInstance().getMacFilterNames());
        timer.cancel();
    }


    @Override
    public void onItemClicked(final ScanResult network) {
        InfoDialogFragment mInfoDialogFragment = new InfoDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.KEY_SCAN_RESULT, network);
        mInfoDialogFragment.setArguments(bundle);
        NavigationManager.getInstance(this).showDialogFragment(mInfoDialogFragment, InfoDialogFragment.TAG);

    }
}
