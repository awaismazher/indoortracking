package demo.vend.wifidistancecalculator.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

import demo.vend.wifidistancecalculator.ApplicationState;
import demo.vend.wifidistancecalculator.utils.Constants;


public class AppPreference {

    private static SharedPreferences sharedPreferences = null;

    private AppPreference() {
    }

    public static void initPreference(final Context mContext) {
        try {
            AppPreference.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        } catch (final Exception exception) {
            logException(exception);
        }
    }

    public static HashMap<String, String> getHashMap(Context context, String key) {
        try {
            if (AppPreference.sharedPreferences == null) {
                AppPreference.initPreference(context);
            }
            Gson gson = new Gson();
            String json = AppPreference.sharedPreferences.getString(key, "");
            return gson.fromJson(json,
                    new TypeToken<HashMap<String, String>>() {
                    }.getType());

        } catch (final Exception exception) {
            logException(exception);
        }
        return null;
    }

    public static void saveHashMap(Context context, String key, HashMap<String, String> map) {
        try {
            if (AppPreference.sharedPreferences == null) {
                AppPreference.initPreference(context);
            }
            if (AppPreference.sharedPreferences != null) {
                final Editor editing = AppPreference.sharedPreferences.edit();
                Gson gson = new Gson();
                String json = gson.toJson(map);
                editing.putString(key, json);
                editing.commit();
            }
        } catch (final Exception exception) {
            logException(exception);
        }
    }


    public static void saveString(final Context context, final String value, final String key) {
        try {
            if (AppPreference.sharedPreferences == null) {
                AppPreference.initPreference(context);
            }
            if (AppPreference.sharedPreferences != null) {
                editStringPreferenceValue(value, key);
            }
        } catch (final Exception exception) {
            logException(exception);
        }
    }
    public static void saveBoolean(final Context context, final boolean value, final String key) {
        try {
            if (AppPreference.sharedPreferences == null) {
                AppPreference.initPreference(context);
            }
            if (AppPreference.sharedPreferences != null) {
                editBooleanPreferenceValue(value, key);
            }
        } catch (final Exception exception) {
            logException(exception);
        }
    }

    public static void logException(Exception exception) {
        exception.printStackTrace();
    }

    protected static void editStringPreferenceValue(final String value, final String key) {
        final Editor editing = AppPreference.sharedPreferences.edit();
        try {
            editing.remove(key);
        } catch (final Exception exception) {
            logException(exception);
        }
        editing.putString(key, value);
        editing.commit();
    }
    protected static void editBooleanPreferenceValue(final boolean value, final String key) {
        final Editor editing = AppPreference.sharedPreferences.edit();
        try {
            editing.remove(key);
        } catch (final Exception exception) {
            logException(exception);
        }
        editing.putBoolean(key, value);
        editing.commit();
    }

    public static String getString(final Context context, final String key, String defaultValue) {

        try {
            if (AppPreference.sharedPreferences == null) {
                AppPreference.initPreference(context);
            }
            return AppPreference.sharedPreferences.getString(key, defaultValue);
        } catch (final Exception exception) {
            logException(exception);
        }
        return null;
    }
    public static boolean getBoolean(final Context context, final String key, boolean defaultValue) {

        try {
            if (AppPreference.sharedPreferences == null) {
                AppPreference.initPreference(context);
            }
            return AppPreference.sharedPreferences.getBoolean(key, defaultValue);
        } catch (final Exception exception) {
            logException(exception);
        }
        return false;
    }

    public static void clear() {
        try {
            if (AppPreference.sharedPreferences == null) {
                AppPreference.initPreference(ApplicationState.getInstance());
            }
            if (AppPreference.sharedPreferences != null) {
                final Editor editing = AppPreference.sharedPreferences.edit();
                editing.remove(Constants.KEY_MAC_ADDRESS);
                editing.commit();
            }
        } catch (final Exception exception) {
            logException(exception);
        }
    }
}