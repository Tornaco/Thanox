package github.tornaco.android.nitro.framework.host.install.util;

import android.os.Build;

import com.elvishew.xlog.XLog;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import util.IoUtils;


public class InterpretDex2OatHelper {

    private static final String INTERPRET_LOCK_FILE_NAME = "InterpretDex2OatHelper.lock";

    public static void interpretDex2Oat(String dexFilePath, String oatFilePath, String targetISA) throws IOException {

        XLog.d("interpretDex2Oat, targetISA=%s", targetISA);

        // add process lock for interpret mode
        final File oatFile = new File(oatFilePath);
        if (!oatFile.exists()) {
            oatFile.getParentFile().mkdirs();
        }

        File lockFile = new File(oatFile.getParentFile(), INTERPRET_LOCK_FILE_NAME);
        ShareFileLockHelper fileLock = null;
        try {
            fileLock = ShareFileLockHelper.getFileLock(lockFile);

            final List<String> commandAndParams = new ArrayList<>();
            commandAndParams.add("dex2oat");
            // for 7.1.1, duplicate class fix
            if (Build.VERSION.SDK_INT >= 24) {
                commandAndParams.add("--runtime-arg");
                commandAndParams.add("-classpath");
                commandAndParams.add("--runtime-arg");
                commandAndParams.add("&");
            }
            commandAndParams.add("--dex-file=" + dexFilePath);
            commandAndParams.add("--oat-file=" + oatFilePath);
            commandAndParams.add("--instruction-set=" + targetISA);
            if (Build.VERSION.SDK_INT > 25) {
                commandAndParams.add("--compiler-filter=quicken");
            } else {
                commandAndParams.add("--compiler-filter=interpret-only");
            }

            final ProcessBuilder pb = new ProcessBuilder(commandAndParams);
            pb.redirectErrorStream(true);
            final Process dex2oatProcess = pb.start();
            StreamConsumer.consumeInputStream(dex2oatProcess.getInputStream());
            StreamConsumer.consumeInputStream(dex2oatProcess.getErrorStream());
            try {
                final int ret = dex2oatProcess.waitFor();
                if (ret != 0) {
                    throw new IOException("dex2oat works unsuccessfully, exit code: " + ret);
                }
            } catch (InterruptedException e) {
                throw new IOException("dex2oat is interrupted, msg: " + e.getMessage(), e);
            }
        } finally {
            IoUtils.closeQuietly(fileLock);
        }
    }

    private static class StreamConsumer {
        static final Executor STREAM_CONSUMER = Executors.newSingleThreadExecutor();

        static void consumeInputStream(final InputStream is) {
            STREAM_CONSUMER.execute(() -> {
                if (is == null) {
                    return;
                }
                final byte[] buffer = new byte[256];
                try {
                    while ((is.read(buffer)) > 0) {
                        // Noop.
                    }
                } catch (IOException ignored) {
                    // Ignored.
                } finally {
                    try {
                        is.close();
                    } catch (Exception ignored) {
                        // Ignored.
                    }
                }
            });
        }
    }
}