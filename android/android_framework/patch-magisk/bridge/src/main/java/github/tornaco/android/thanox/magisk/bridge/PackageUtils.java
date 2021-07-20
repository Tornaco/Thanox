package github.tornaco.android.thanox.magisk.bridge;

import android.content.pm.PackageParser;
import android.os.Environment;
import android.util.Log;

import java.io.File;

public class PackageUtils {
  /** Directory where installed applications are stored */
  private static final File APP_INSTALL_DIR = new File(Environment.getDataDirectory(), "app");

  public static File getApkPath(String pkgName) {
    final File[] files = APP_INSTALL_DIR.listFiles();
    if (files == null || files.length == 0) {
      return null;
    }
    for (File file : files) {
      final boolean isPackage =
          (isApkFile(file) || file.isDirectory()) && !isStageName(file.getName());
      if (!isPackage) {
        // Ignore entries which are not packages
        continue;
      }
      PackageParser.PackageLite lite = parseApk(file);
      if (lite != null && pkgName.equals(lite.packageName)) {
        // Find apk.
        return findFirstApkFile(new File(lite.codePath));
      }
    }
    return null;
  }

  private static File findFirstApkFile(File file) {
    if (isApkFile(file)) return file;
    if (!file.isDirectory()) return null;
    final File[] files = file.listFiles();
    if (files == null || files.length == 0) {
      return null;
    }
    for (File f : files) {
      if (isApkFile(f)) return f;
    }
    return null;
  }

  // PackageInstallerService
  private static boolean isStageName(String name) {
    boolean isFile = name.startsWith("vmdl") && name.endsWith(".tmp");
    boolean isContainer = name.startsWith("smdl") && name.endsWith(".tmp");
    boolean isLegacyContainer = name.startsWith("smdl2tmp");
    return isFile || isContainer || isLegacyContainer;
  }

  private static boolean isApkFile(File file) {
    return PackageParser.isApkFile(file);
  }

  private static PackageParser.PackageLite parseApk(File file) {
    try {
      return PackageParser.parsePackageLite(file, 0 /* No flags */);
    } catch (PackageParser.PackageParserException e) {
      Log.e("parseApk got error: %s", Log.getStackTraceString(e));
    }
    return null;
  }
}
