package github.tornaco.android.thanos.core.backup;

import android.os.ParcelFileDescriptor;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@AllArgsConstructor
public class BackupAgent {
    private final IBackupAgent server;

    @SneakyThrows
    public void performBackup(ParcelFileDescriptor init) {
        server.performBackup(init);
    }

    @SneakyThrows
    public void performRestore(ParcelFileDescriptor pfd) {
        server.performRestore(pfd);
    }

    @SneakyThrows
    public boolean restoreDefault() {
        return server.restoreDefault();
    }
}
