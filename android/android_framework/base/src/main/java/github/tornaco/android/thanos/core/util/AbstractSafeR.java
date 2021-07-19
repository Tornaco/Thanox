package github.tornaco.android.thanos.core.util;

import android.util.Log;

public abstract class AbstractSafeR implements Runnable {
  private String name = "";

  public AbstractSafeR setName(String name) {
    this.name = name;
    return this;
  }

  @Override
  public final void run() {
    try {
      runSafety();
    } catch (Throwable err) {
      Log.e("AbstractSafeR", name + " -AbstractSafeR err: " + Log.getStackTraceString(err));
    }
  }

  public abstract void runSafety();
}
