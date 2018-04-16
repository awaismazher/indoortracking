package demo.vend.wifidistancecalculator.fragments;

import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Objects;

import demo.vend.wifidistancecalculator.ApplicationState;
import demo.vend.wifidistancecalculator.R;
import demo.vend.wifidistancecalculator.storage.AppPreference;
import demo.vend.wifidistancecalculator.utils.Constants;
import demo.vend.wifidistancecalculator.utils.DialogUtils;


public class InfoDialogFragment extends BaseDialogFragment implements View.OnClickListener{

    public static final String TAG = InfoDialogFragment.class.getSimpleName();
    private TextView tvFloorName;
    private TextView tvRegionName;
    private boolean isFloorNameChanged, isRegionNameChanged;
    private String[] floorNames;
    private  String[] regionNames;
    private ScanResult scanResult;

    public InfoDialogFragment(){
        //Empty constructor required
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mDialogView = inflater.inflate(R.layout.dialog_info, container);
        mDialog = getDialog();
        floorNames = Objects.requireNonNull(getContext()).getResources().getStringArray(R.array.floor_fields);
        regionNames = Objects.requireNonNull(getContext()).getResources().getStringArray(R.array.floor_regions);
        if(getArguments()!=null){
            if(getArguments().containsKey(Constants.KEY_SCAN_RESULT))
                scanResult = getArguments().getParcelable(Constants.KEY_SCAN_RESULT);
        }
        return mDialogView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvFloorName = view.findViewById(R.id.tv_floor_name_value);
        tvRegionName = view.findViewById(R.id.tv_region_name_value);
        view.findViewById(R.id.iv_floor_edit).setOnClickListener(this);
        view.findViewById(R.id.iv_floor_remove).setOnClickListener(this);
        view.findViewById(R.id.iv_region_edit).setOnClickListener(this);
        view.findViewById(R.id.iv_region_remove).setOnClickListener(this);
        view.findViewById(R.id.btn_cancel).setOnClickListener(this);
        populateData();
    }

    private void populateData() {
        HashMap<String, String> macFilterName = ApplicationState.getInstance().getMacFilterNames();
        HashMap<String, String> floorRegionName = ApplicationState.getInstance().getFloorRegionNames();
        if(macFilterName!=null){
            if(macFilterName.containsKey(scanResult.BSSID))
                tvFloorName.setText(macFilterName.get(scanResult.BSSID));
            else
                tvFloorName.setText(Constants.NOT_AVAILABLE_STRING);
        }else
            tvFloorName.setText(Constants.NOT_AVAILABLE_STRING);
        if(floorRegionName!=null){
            if(floorRegionName.containsKey(scanResult.BSSID))
                tvRegionName.setText(floorRegionName.get(scanResult.BSSID));
            else
                tvRegionName.setText(Constants.NOT_AVAILABLE_STRING);
        }else
            tvRegionName.setText(Constants.NOT_AVAILABLE_STRING);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_floor_edit:
                DialogUtils.showRadioButtonDialog(getContext(), getString(R.string.choose_floor_title),
                        floorNames, 0, (dialog, which) -> {
                            tvFloorName.setText(floorNames[which]);
                            updateName(floorNames[which],scanResult);
                        });
                break;
            case R.id.iv_floor_remove:
                DialogUtils.showDialog(getContext(), getString(R.string.removal_confirmation_title),
                        getString(R.string.removal_confirmation_desctiption),
                        (dialog, which) -> removeName(scanResult));
                break;
            case R.id.iv_region_edit:
                DialogUtils.showRadioButtonDialog(getContext(), getString(R.string.choose_floor_region),
                        regionNames, 0, (dialog, which) -> {
                            tvRegionName.setText(regionNames[which]);
                            updateRegion(scanResult, regionNames[which]);
                        });
                break;
            case R.id.iv_region_remove:
                DialogUtils.showDialog(getContext(), getString(R.string.removal_confirmation_title),
                        getString(R.string.removal_confirmation_desctiption),
                        (dialog, which) -> removeRegion(scanResult));
                break;
            case R.id.btn_cancel:
                getDialog().dismiss();
                break;
        }
    }
    private void removeName(ScanResult network) {
        ApplicationState.getInstance().getMacFilterNames().remove(network.BSSID);
        AppPreference.saveHashMap(getContext(), Constants.KEY_MAC_ADDRESS,
                ApplicationState.getInstance().getMacFilterNames());
        tvFloorName.setText(Constants.NOT_AVAILABLE_STRING);
    }

    private void updateName(String name, ScanResult network) {
        if(!name.isEmpty()) {
            ApplicationState.getInstance().getMacFilterNames().put(network.BSSID, name);
            AppPreference.saveHashMap(getContext(), Constants.KEY_MAC_ADDRESS, ApplicationState.getInstance().getMacFilterNames());
        }
    }

    private void removeRegion(ScanResult scanResult) {
        ApplicationState.getInstance().getFloorRegionNames().remove(scanResult.BSSID);
        AppPreference.saveHashMap(getContext(), Constants.KEY_FLOOR_REGIONS,
                ApplicationState.getInstance().getFloorRegionNames());
        tvRegionName.setText(Constants.NOT_AVAILABLE_STRING);
    }

    private void updateRegion(ScanResult scanResult, String value) {
        if(!value.isEmpty()) {
            ApplicationState.getInstance().getFloorRegionNames().put(scanResult.BSSID, value);
            AppPreference.saveHashMap(getContext(), Constants.KEY_FLOOR_REGIONS,
                    ApplicationState.getInstance().getFloorRegionNames());
        }
    }
}
