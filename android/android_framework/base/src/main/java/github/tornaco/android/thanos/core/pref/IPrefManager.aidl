package github.tornaco.android.thanos.core.pref;

import github.tornaco.android.thanos.core.pref.IPrefChangeListener;

interface IPrefManager {
    boolean putInt(String key, int value);
    int getInt(String key, int def);

    boolean putString(String key, String value);
    String getString(String key, String def);

    boolean putBoolean(String key, boolean value);
    boolean getBoolean(String key, boolean def);

    boolean putLong(String key, long value);
    long getLong(String key, long def);

   boolean registerSettingsChangeListener(in IPrefChangeListener listener);
   boolean unRegisterSettingsChangeListener(in IPrefChangeListener listener);
}