package github.tornaco.android.thanos.core.backup;

import github.tornaco.android.thanos.core.backup.IFileDescriptorConsumer;

// oneway
interface IFileDescriptorInitializer {
    oneway void initParcelFileDescriptor(String domain, String path, in IFileDescriptorConsumer consumer);
}
