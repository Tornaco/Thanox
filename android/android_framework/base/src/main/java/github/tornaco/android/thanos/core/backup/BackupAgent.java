package github.tornaco.android.thanos.core.backup;

import android.os.ParcelFileDescriptor;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@AllArgsConstructor
public class BackupAgent {
    private final IBackupAgent server;

    @SneakyThrows
    public void performBackup(IFileDescriptorInitializer init, String domain, String path, IBackupCallback callback) {
        server.performBackup(init, domain, path, callback);
    }

    @SneakyThrows
    public void performRestore(ParcelFileDescriptor pfd, String domain, String path, IBackupCallback callback) {
        server.performRestore(pfd, domain, path, callback);
    }

    @SneakyThrows
    public boolean restoreDefault() {
        return server.restoreDefault();
    }
}
