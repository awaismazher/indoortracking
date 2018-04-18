package demo.vend.wifidistancecalculator.manager;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import demo.vend.wifidistancecalculator.activity.BaseActivity;
import demo.vend.wifidistancecalculator.fragments.BaseDialogFragment;
import demo.vend.wifidistancecalculator.fragments.BaseFragment;
import demo.vend.wifidistancecalculator.utils.Constants;


public class NavigationManager {

    public static String CURRENT_FRAGMENT_TAG = Constants.EMPTY_STRING;

    private static BaseActivity mActivity;
    private static NavigationManager mInstance;
    private static FragmentManager mFragmentManager;
    private static FragmentTransaction mFragmentTransaction;

    private NavigationManager(BaseActivity mActivity){
        this.mActivity = mActivity;
        mFragmentManager = mActivity.getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
    }

    public static final NavigationManager getInstance(BaseActivity activity){
        if(mInstance == null)
            return new NavigationManager(activity);
        return mInstance;
    }

    public static final void navigateTo(BaseFragment mFragment, String mTag){
        CURRENT_FRAGMENT_TAG = mTag;
       // mFragmentTransaction.replace(R.id.main_frame_layout, mFragment, mTag);
        navigate(mFragmentTransaction);
    }

    public static final void navigateWithBackStackTo(BaseFragment mFragment, String mTag){
        CURRENT_FRAGMENT_TAG = mTag;
       //mFragmentTransaction.replace(R.id.main_frame_layout, mFragment, mTag);
        mFragmentTransaction.addToBackStack(mTag);
        navigate(mFragmentTransaction);
    }

    private static final void navigate(FragmentTransaction mFragmentTransaction){
        try {
            mFragmentTransaction.commitAllowingStateLoss();
        } catch (IllegalStateException e){
            e.printStackTrace();
        }
    }

    public static final void clearAllFragmentsFromBackStack(){
        FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
        for(int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
            fragmentManager.popBackStack();
        }
    }

    public static final void showDialogFragment(BaseDialogFragment mBaseDialogFragment, String mTag){
        mBaseDialogFragment.show(mFragmentManager, mTag);
    }

}
