package github.tornaco.android.thanox.proxy;

import com.elvishew.xlog.XLog;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;

import github.tornaco.android.thanos.core.util.FileUtils;

public abstract class BaseProxyFactory<T> {
  public final T newProxy(T original) {
    try {
      return onCreateProxy(original, dxCacheDir());
    } catch (Throwable e) {
      XLog.e(e, "Fail create proxy by %s for %s", getClass(), original);
      return null;
    }
  }

  protected abstract T onCreateProxy(T original, File dexCacheDir) throws Exception;

  private File dxCacheDir() throws IOException {
    // Dex cache dir.
    File dx = new File(github.tornaco.android.thanos.core.T.baseServerTmpDir(), "dx");
    // FileUtils.deleteDirQuiet(dx);
    //noinspection UnstableApiUsage
    Files.createParentDirs(new File(dx, "dummy"));
    return dx;
  }
}
