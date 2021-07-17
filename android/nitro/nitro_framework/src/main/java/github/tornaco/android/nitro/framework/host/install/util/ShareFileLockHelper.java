package github.tornaco.android.nitro.framework.host.install.util;

import com.elvishew.xlog.XLog;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileLock;


public class ShareFileLockHelper implements Closeable {

    private static final int MAX_LOCK_ATTEMPTS = 3;
    private static final int LOCK_WAIT_EACH_TIME = 10;

    private final FileOutputStream outputStream;
    private final FileLock fileLock;

    private ShareFileLockHelper(File lockFile) throws IOException {
        outputStream = new FileOutputStream(lockFile);

        int numAttempts = 0;
        boolean isGetLockSuccess;
        FileLock localFileLock = null;
        //just wait twice,
        Exception saveException = null;
        while (numAttempts < MAX_LOCK_ATTEMPTS) {
            numAttempts++;
            try {
                localFileLock = outputStream.getChannel().lock();
                isGetLockSuccess = (localFileLock != null);
                if (isGetLockSuccess) {
                    break;
                }
                //it can just sleep 0, afraid of cpu scheduling
                Thread.sleep(LOCK_WAIT_EACH_TIME);

            } catch (Exception e) {
                XLog.e(e, "getInfoLock Thread failed time: %d", LOCK_WAIT_EACH_TIME);
            }
        }

        if (localFileLock == null) {
            throw new IOException("Tinker Exception:FileLockHelper lock file failed: " + lockFile.getAbsolutePath(),
                    saveException);
        }
        fileLock = localFileLock;
    }

    public static ShareFileLockHelper getFileLock(File lockFile) throws IOException {
        return new ShareFileLockHelper(lockFile);
    }

    @Override
    public void close() throws IOException {
        try {
            if (fileLock != null) {
                fileLock.release();
            }
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }
}