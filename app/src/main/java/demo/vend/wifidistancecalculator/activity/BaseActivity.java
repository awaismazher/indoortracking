package demo.vend.wifidistancecalculator.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import demo.vend.wifidistancecalculator.storage.AppPreference;

public class BaseActivity extends AppCompatActivity {
    public static Context mContext;


    @Override
    protected void onStart() {
        super.onStart();
        mContext = this;
        AppPreference.initPreference(mContext);
    }


}
