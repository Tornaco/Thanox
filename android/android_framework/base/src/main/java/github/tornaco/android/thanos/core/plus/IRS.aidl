package github.tornaco.android.thanos.core.plus;

import github.tornaco.android.thanos.core.plus.ICallback;

// Remote Server
interface IRS {
    // Bind code.
    void bc(in String code, in String deviceId, in ICallback cb);

    // Verify binding.
    void vb(in String code, in String deviceId, in ICallback cb);
}