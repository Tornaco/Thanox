//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package github.tornaco.android.thanos.core.util;

import android.os.Debug;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;

public final class MemInfoReader {
    final long[] mInfos = new long[15];

    public MemInfoReader() {
    }

    public void readMemInfo() {
        ThreadPolicy savedPolicy = StrictMode.allowThreadDiskReads();

        try {
            Debug.getMemInfo(this.mInfos);
        } finally {
            StrictMode.setThreadPolicy(savedPolicy);
        }

    }

    public long getTotalSize() {
        return this.mInfos[0] * 1024L;
    }

    public long getFreeSize() {
        return this.mInfos[1] * 1024L;
    }

    public long getCachedSize() {
        return this.getCachedSizeKb() * 1024L;
    }

    public long getKernelUsedSize() {
        return this.getKernelUsedSizeKb() * 1024L;
    }

    public long getTotalSizeKb() {
        return this.mInfos[0];
    }

    public long getFreeSizeKb() {
        return this.mInfos[1];
    }

    public long getCachedSizeKb() {
        return this.mInfos[2] + this.mInfos[6] + this.mInfos[3] - this.mInfos[11];
    }

    public long getKernelUsedSizeKb() {
        return this.mInfos[4] + this.mInfos[7] + this.mInfos[12] + this.mInfos[13] + this.mInfos[14];
    }

    public long getSwapTotalSizeKb() {
        return this.mInfos[8];
    }

    public long getSwapFreeSizeKb() {
        return this.mInfos[9];
    }

    public long getZramTotalSizeKb() {
        return this.mInfos[10];
    }

    public long[] getRawInfo() {
        return this.mInfos;
    }
}
