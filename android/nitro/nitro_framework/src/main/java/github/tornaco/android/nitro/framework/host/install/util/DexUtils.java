package github.tornaco.android.nitro.framework.host.install.util;

import android.content.Context;

import com.elvishew.xlog.XLog;

import java.io.IOException;

import dalvik.system.DexFile;
import github.tornaco.android.nitro.framework.plugin.PluginFile;

public class DexUtils {

  public static boolean optimize(Context context, PluginFile pluginFile) {
    try {
      return optimize0(context, pluginFile);
    } catch (Throwable e) {
      XLog.e(e);
      return false;
    }
  }

  private static boolean optimize0(Context context, PluginFile pluginFile) {
    String pluginPath = pluginFile.getOriginFile();
    String oatFilePath = pluginFile.getDexFile();

    if (ShareInternals.isVmArt()) {
      try {
        InterpretDex2OatHelper.interpretDex2Oat(
            pluginPath, oatFilePath, ShareInternals.getCurrentInstructionSet());
        XLog.d("optimize complete: %s", pluginFile.getPackageName());
        return true;
      } catch (Exception e) {
        XLog.e("optimize", e);
      }
    } else {
      try {
        DexFile dexFile = DexFile.loadDex(pluginPath, oatFilePath, 0);
        return dexFile != null;
      } catch (IOException e) {
        XLog.e("DexFile.loadDex", e);
      }
    }
    return false;
  }

  // 2020-09-26 21:30:56.786 9977-10033/github.tornaco.android.thanos E/ThanoxApp: optimize
  //    java.io.IOException: Cannot run program "dex2oat": error=13, Permission denied
  //        at java.lang.ProcessBuilder.start(ProcessBuilder.java:1050)
  //        at
  // github.tornaco.android.nitro.framework.host.install.util.InterpretDex2OatHelper.interpretDex2Oat(:57)
  //        at github.tornaco.android.nitro.framework.host.install.util.DexUtils.optimize(:20)
  //        at github.tornaco.android.nitro.framework.host.install.Installer.install(:176)
  //        at github.tornaco.android.nitro.framework.host.install.Installer.install(:93)
  //        at github.tornaco.android.nitro.framework.host.install.Installer.install(:83)
  //        at github.tornaco.android.nitro.framework.Nitro.b(:33)
  //        at github.tornaco.android.nitro.framework.b.run(Unknown Source:6)
  //        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1167)
  //        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:641)
  //        at java.lang.Thread.run(Thread.java:923)
  //     Caused by: java.io.IOException: error=13, Permission denied
  //        at java.lang.UNIXProcess.forkAndExec(Native Method)
  //        at java.lang.UNIXProcess.<init>(UNIXProcess.java:133)
  //        at java.lang.ProcessImpl.start(ProcessImpl.java:141)
  //        at java.lang.ProcessBuilder.start(ProcessBuilder.java:1029)
  //        at
  // github.tornaco.android.nitro.framework.host.install.util.InterpretDex2OatHelper.interpretDex2Oat(:57)
  //        at github.tornaco.android.nitro.framework.host.install.util.DexUtils.optimize(:20)
  //        at github.tornaco.android.nitro.framework.host.install.Installer.install(:176)
  //        at github.tornaco.android.nitro.framework.host.install.Installer.install(:93)
  //        at github.tornaco.android.nitro.framework.host.install.Installer.install(:83)
  //        at github.tornaco.android.nitro.framework.Nitro.b(:33)
  //        at github.tornaco.android.nitro.framework.b.run(Unknown Source:6)
  //        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1167)
  //        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:641)
  //        at java.lang.Thread.run(Thread.java:923)
}
