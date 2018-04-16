package demo.vend.wifidistancecalculator.manager;

import android.net.wifi.ScanResult;

import java.util.Comparator;

public class DistanceCompartor implements Comparator<ScanResult> {
    @Override
    public int compare(ScanResult a, ScanResult b) {
        return getDistanceFromRouterDouble(a) < getDistanceFromRouterDouble(b) ? -1 : getDistanceFromRouterDouble(a) == getDistanceFromRouterDouble(b) ? 0 : 1;
    }
    private double getDistanceFromRouterDouble(ScanResult network) {
        double exp = (27.55 - (20 * Math.log10(network.frequency)) + Math.abs(network.level)) / 20.0;
        return Math.pow(10.0, exp);
    }
}
