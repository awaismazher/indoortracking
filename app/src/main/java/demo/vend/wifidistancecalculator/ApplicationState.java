package demo.vend.wifidistancecalculator;

import android.app.Application;
import android.net.wifi.ScanResult;

import java.util.HashMap;
import java.util.List;

public class ApplicationState extends Application {

    private static ApplicationState sApplicationState;
    private HashMap<String,String > macFilterNames;
    private HashMap<String, String> floorRegionNames;
    private List<ScanResult> scanResults;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    public static ApplicationState getInstance(){
        if(sApplicationState==null){
          sApplicationState = new ApplicationState();
        }
        return sApplicationState;
    }

    public HashMap<String, String> getMacFilterNames() {
        if(macFilterNames==null)
            macFilterNames = new HashMap<>();
        return macFilterNames;
    }

    public void setMacFilterNames(HashMap<String, String> macFilterNames) {
        this.macFilterNames = macFilterNames;
    }

    public HashMap<String, String> getFloorRegionNames() {
        if(floorRegionNames == null)
            floorRegionNames = new HashMap<>();
        return floorRegionNames;
    }

    public void setFloorRegionNames(HashMap<String, String> floorRegionNames) {
        this.floorRegionNames = floorRegionNames;
    }

    public void setScanResults(List<ScanResult> scanResults) {
        this.scanResults = scanResults;
    }

    public List<ScanResult> getScanResults() {
        return scanResults;
    }
}
