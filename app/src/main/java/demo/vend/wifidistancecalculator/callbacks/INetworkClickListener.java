package demo.vend.wifidistancecalculator.callbacks;

import android.net.wifi.ScanResult;

public interface INetworkClickListener {
    void onItemClicked(ScanResult network);
}
