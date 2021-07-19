package github.tornaco.android.thanos.core.persist.i;

public interface Repo {
    void reload();

    void reloadAsync();

    void flush();

    void flushAsync();

    String name();
}
