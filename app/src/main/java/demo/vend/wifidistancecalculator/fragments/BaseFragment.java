package demo.vend.wifidistancecalculator.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

public class BaseFragment extends Fragment {

    protected Context mContext;
    protected View mFragmentView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext = context;

    }
}
