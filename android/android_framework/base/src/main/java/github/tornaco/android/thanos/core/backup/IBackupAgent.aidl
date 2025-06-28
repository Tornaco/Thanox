package github.tornaco.android.thanos.core.backup;

interface IBackupAgent {
   oneway void performBackup(in ParcelFileDescriptor pfd);

   oneway void performRestore(in ParcelFileDescriptor pfd);

   boolean restoreDefault();
}
