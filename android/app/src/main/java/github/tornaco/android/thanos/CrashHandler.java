package github.tornaco.android.thanos;

import android.app.Application;
import android.os.Process;
import android.util.Log;

import com.google.common.io.Files;
import com.osama.firecrasher.FireCrasher;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import github.tornaco.android.thanos.core.util.DateUtils;
import github.tornaco.android.thanos.core.util.DevNull;
import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

@SuppressWarnings("UnstableApiUsage")
class CrashHandler {

    static void install(Application application) {
        FireCrasher.INSTANCE.install(application, (throwable, activity) -> {
            Object dev = Completable.fromRunnable(() -> {
                try {
                    File logFile = new File(application.getExternalCacheDir(), "logs/" + "CRASH_" + DateUtils.formatForFileName(System.currentTimeMillis()));
                    Files.createParentDirs(logFile);
                    Files.asByteSink(logFile)
                            .asCharSink(Charset.defaultCharset())
                            .write(Log.getStackTraceString(throwable));
                } catch (IOException ignored) {
                } finally {
                }
            }).subscribeOn(Schedulers.io()).subscribe(() -> Process.killProcess(Process.myPid()));
            DevNull.accept(dev);
        });
    }
}
