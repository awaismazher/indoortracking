package demo.vend.wifidistancecalculator.fragments;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.ViewGroup;

import demo.vend.wifidistancecalculator.R;
import demo.vend.wifidistancecalculator.activity.BaseActivity;

public class BaseDialogFragment extends DialogFragment {

    protected Dialog mDialog;
    protected View mDialogView;
    protected BaseActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BaseActivity)context;
    }

    @Override
    public void onResume() {
        super.onResume();
       // adjustInfoDialogWidth();
    }

    private void adjustInfoDialogWidth(){
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = (int)getResources().getDimension(R.dimen.dp_320);
        mDialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }
    @Override
    public void onStart()
    {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
}
