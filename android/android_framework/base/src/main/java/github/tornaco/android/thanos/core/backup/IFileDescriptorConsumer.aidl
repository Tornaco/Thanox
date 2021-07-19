package github.tornaco.android.thanos.core.backup;

// oneway
interface IFileDescriptorConsumer {
    // IO.
    oneway void acceptAppParcelFileDescriptor(in ParcelFileDescriptor pfd);
}
