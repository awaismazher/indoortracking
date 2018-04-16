package demo.vend.wifidistancecalculator.adapter;

import android.net.wifi.ScanResult;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import demo.vend.wifidistancecalculator.ApplicationState;
import demo.vend.wifidistancecalculator.callbacks.INetworkClickListener;
import demo.vend.wifidistancecalculator.R;
import demo.vend.wifidistancecalculator.manager.DistanceCompartor;
import demo.vend.wifidistancecalculator.utils.Constants;

public class NetworkListAdapter extends RecyclerView.Adapter<NetworkListAdapter.MyViewHolder> {
    List<ScanResult> scanResultList;
    INetworkClickListener iNetworkClickListener;

    public NetworkListAdapter(List<ScanResult> scanResultList, INetworkClickListener iNetworkClickListener) {
        Collections.sort(scanResultList, new DistanceCompartor());
        this.scanResultList = scanResultList;
        this.iNetworkClickListener = iNetworkClickListener;
    }

    @NonNull
    @Override
    public NetworkListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.network_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NetworkListAdapter.MyViewHolder holder, final int position) {
        holder.tvList.setText(getNetworkDetails(scanResultList.get(position)));
        holder.tvList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iNetworkClickListener.onItemClicked(scanResultList.get(position));
            }
        });
    }

    private String getNetworkDetails(ScanResult network) {
        return "SSID Name:  " + network.SSID + "\n" +
                getMacORName(network) + "\n" +
                "Region: " + getRegionName(network) + "\n" +
                "Distance :" + getDistanceFromRouter(network) + "m" + "\n" +
                "Strength:  " + network.level + "dbm" + "\n" ;
    }

    private String getRegionName(ScanResult network) {
        HashMap<String, String> floorRegionName = ApplicationState.getInstance().getFloorRegionNames();
        if (floorRegionName != null) {
            if (floorRegionName.containsKey(network.BSSID))
                return floorRegionName.get(network.BSSID);
        }
        return Constants.NOT_AVAILABLE_STRING;
    }

    private String getMacORName(ScanResult network) {
        HashMap<String, String> networkNames = ApplicationState.getInstance().getMacFilterNames();
        if (networkNames.get(network.BSSID) == null)
            return "Mac:  " + network.BSSID.toUpperCase();
        else
            return "Floor Name:  " + networkNames.get(network.BSSID);
    }

    @Override
    public int getItemCount() {
        return scanResultList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvList;

        public MyViewHolder(View view) {
            super(view);
            tvList = view.findViewById(R.id.tvList);
        }
    }

    private String getDistanceFromRouter(ScanResult network) {
        double exp = ((27.55 - (20 * Math.log10(network.frequency)) + Math.abs(network.level)) / 20.0);
        double distanceM = Math.pow(10.0, ((27.55 - (20 * Math.log10(network.frequency)) + Math.abs(network.level)) / 20.0));

        DecimalFormat lFormatter = new DecimalFormat("##0.0000");
        return lFormatter.format(distanceM);
    }
}
