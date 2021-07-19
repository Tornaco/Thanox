package github.tornaco.android.thanos.core.backup;

import github.tornaco.android.thanos.core.backup.IBackupCallback;
import github.tornaco.android.thanos.core.backup.IFileDescriptorInitializer;

interface IBackupAgent {
   oneway void performBackup(in IFileDescriptorInitializer init, String domain, String path, in IBackupCallback callback);

   oneway void performRestore(in ParcelFileDescriptor pfd, String domain, String path, in IBackupCallback callback);

   boolean restoreDefault();
}
