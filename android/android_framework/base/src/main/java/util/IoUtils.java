package util;


import java.io.Closeable;
import java.io.IOException;

public class IoUtils {
    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignored) {

            }
        }
    }

    public static void closeQuietly(Process process) {
        if (process != null) {
            try {
                process.destroy();
            } catch (Throwable ignored) {

            }
        }
    }
}
