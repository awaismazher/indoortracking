package demo.vend.wifidistancecalculator.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;



public class DialogUtils {

    private static Dialog sProgressDialog;

    /**
     * Show Dialog with OK Button
     */
    public static void showDialog(final Context context, final String title, final String msg) {
        showDialog(context, title, msg, null);
    }
    public static void showErrorDialog(final Context context){
        showDialog(context, Constants.EMPTY_STRING, "Something went wrong");
    }

    public static void showDialog(final Context context, final String title, final String msg, final DialogInterface.OnClickListener clickListener) {
        showDialogWithCustomPositiveButtonName(context, title, msg, clickListener, android.R.string.ok);
    }
    public static void showDialogWithCustomPositiveButtonName(final Context context, final String title, final String msg, final DialogInterface.OnClickListener clickListener, final int resourceId) {

        //Run On UI Thread
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(title);
                builder.setMessage(msg);

                String positiveText = context.getString(resourceId);
                builder.setPositiveButton(positiveText,
                        (dialog, which) -> {
                            if (clickListener != null) {
                                clickListener.onClick(dialog, which);
                                dialog.dismiss();
                            }
                        });

                AlertDialog dialog = builder.create();
                // display dialog
                try {
                    dialog.show();
                }catch (IllegalStateException e){
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Show/Hide Progress Dialog
     */
    public static void hideProgressDialog() {
        if (sProgressDialog != null) {
            try {
                sProgressDialog.cancel();
            } catch (Exception e) {
                Log.e(DialogUtils.class.getName(), " progress dialog error");
            }
        }
        sProgressDialog = null;
    }

    public static void showWaitProgressDialog(final Context context) {
        showProgressDialog(context, "wait...");
    }

    public static void showProgressDialog(final Context context, final String message) {
        //Run On UI Thread
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (sProgressDialog != null) {
                    return;
                }
                sProgressDialog = ProgressDialog.show(context, "", message);
//                sProgressDialog.show();
            }
        });
    }

    /**
     * Show Radio Button Dialog
     */
    public static void showRadioButtonDialog(final Context context, final String title,
                                             final CharSequence[] items, final int selectedIndex,
                                             final DialogInterface.OnClickListener clickListener) {

        if (items == null || items.length == 0) {
            return;
        }
        //Run On UI Thread
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                int index = selectedIndex;
                // Creating and Building the Dialog
                AlertDialog radioDialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                if (!TextUtils.isEmpty(title)) {
                    builder.setTitle(title);
                }
                if (selectedIndex <= 0 || selectedIndex >= items.length) {
                    index = 0;
                }
                builder.setSingleChoiceItems(items, index, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                        if (clickListener != null) {
                            clickListener.onClick(dialog, item);
                        }
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                });
                String negativeText = context.getString(android.R.string.cancel);
                builder.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                });
                radioDialog = builder.create();
                radioDialog.show();

            }
        });
    }


}
