package github.tornaco.android.thanos.core.pref;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@AllArgsConstructor
public class PrefManager {
    private IPrefManager server;

    @SneakyThrows
    public boolean putInt(String key, int value) {
        return server.putInt(key, value);
    }

    @SneakyThrows
    public int getInt(String key, int def) {
        return server.getInt(key, def);
    }

    @SneakyThrows
    public boolean putString(String key, String value) {
        return server.putString(key, value);
    }

    @SneakyThrows
    public String getString(String key, String def) {
        return server.getString(key, def);
    }

    @SneakyThrows
    public boolean putBoolean(String key, boolean value) {
        return server.putBoolean(key, value);
    }

    @SneakyThrows
    public boolean getBoolean(String key, boolean def) {
        return server.getBoolean(key, def);
    }

    @SneakyThrows
    public boolean putLong(String key, long value) {
        return server.putLong(key, value);
    }

    @SneakyThrows
    public long getLong(String key, long def) {
        return server.getLong(key, def);
    }

    @SneakyThrows
    public boolean registerSettingsChangeListener(PrefChangeListener listener) {
        return server.registerSettingsChangeListener(listener.getListener());
    }

    @SneakyThrows
    public boolean unRegisterSettingsChangeListener(PrefChangeListener listener) {
        return server.unRegisterSettingsChangeListener(listener.getListener());
    }
}
