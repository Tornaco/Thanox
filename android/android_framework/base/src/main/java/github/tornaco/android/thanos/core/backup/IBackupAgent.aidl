package github.tornaco.android.thanos.core.backup;

interface IBackupAgent {
   void performBackup(in ParcelFileDescriptor pfd);

   void performRestore(in ParcelFileDescriptor pfd);

   boolean restoreDefault();
}
