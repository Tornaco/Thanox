package com.android.dx.stock;

import com.elvishew.xlog.XLog;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;

public abstract class BaseProxyFactory<T> {

    public final T newProxy(T original, File baseDataDir) {
        try {
            return onCreateProxy(original, dxCacheDir(baseDataDir));
        } catch (Throwable e) {
            XLog.e(e, "BaseProxyFactory fail create proxy by %s for %s", getClass(), original);
            return null;
        }
    }

    protected abstract T onCreateProxy(T original, File dexCacheDir) throws Exception;

    private File dxCacheDir(File baseDir) throws IOException {
        // Dex cache dir.
        File dx = new File(baseDir, "dx");
        XLog.i("BaseProxyFactory Using dxCacheDir as dx dir: %s", dx);
        // FileUtils.deleteDirQuiet(dx);
        //noinspection UnstableApiUsage
        Files.createParentDirs(new File(dx, "dummy"));
        return dx;
    }
}
