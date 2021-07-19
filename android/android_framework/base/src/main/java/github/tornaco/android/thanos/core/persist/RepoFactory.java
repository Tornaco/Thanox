package github.tornaco.android.thanos.core.persist;

import android.os.Handler;
import android.os.HandlerThread;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import util.Singleton;

public final class RepoFactory {

    private static final ExecutorService IO = Executors.newSingleThreadExecutor();

    private static final Singleton<RepoFactory> ME = new Singleton<RepoFactory>() {
        @Override
        protected RepoFactory create() {
            return new RepoFactory();
        }
    };

    private final Map<String, StringMapRepo> stringMapRepoCache = new ConcurrentHashMap<>();
    private final Map<String, StringSetRepo> stringSetRepoCache = new ConcurrentHashMap<>();
    private final Map<String, JsonObjectSetRepo> joSetRepoCache = new ConcurrentHashMap<>();
    private final Handler repoHandler;

    private RepoFactory() {
        // Sync in a new handler thread.
        HandlerThread hr = new HandlerThread("RepoFactory");
        hr.start();
        this.repoHandler = new Handler(hr.getLooper());
    }

    public static RepoFactory get() {
        return ME.get();
    }

    public StringMapRepo getOrCreateStringMapRepo(String path) {
        if (stringMapRepoCache.containsKey(path)) {
            return stringMapRepoCache.get(path);
        }
        StringMapRepo repo = new StringMapRepo(new File(path), this.repoHandler, IO);
        stringMapRepoCache.put(path, repo);
        return repo;
    }

    public StringSetRepo getOrCreateStringSetRepo(String path) {
        return getOrCreateStringSetRepo(path, true);
    }

    public StringSetRepo getOrCreateStringSetRepo(String path, boolean useCacheIfAvailable) {
        if (useCacheIfAvailable && stringSetRepoCache.containsKey(path)) {
            return stringSetRepoCache.get(path);
        }
        StringSetRepo repo = new StringSetRepo(new File(path), this.repoHandler, IO);
        if (useCacheIfAvailable) {
            stringSetRepoCache.put(path, repo);
        }
        return repo;
    }

    @SuppressWarnings("unchecked")
    public <T> JsonObjectSetRepo<T> getOrCreateJsonObjectSetRepo(String path, Class<? extends JsonObjectSetRepo<T>> clazz) {
        if (joSetRepoCache.containsKey(path)) {
            return joSetRepoCache.get(path);
        }
        try {
            Constructor<?> constructor = clazz.getConstructor(File.class, Handler.class, ExecutorService.class);
            constructor.setAccessible(true);
            JsonObjectSetRepo<T> repo = (JsonObjectSetRepo<T>) constructor.newInstance(new File(path), this.repoHandler, IO);
            joSetRepoCache.put(path, repo);
            return repo;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
