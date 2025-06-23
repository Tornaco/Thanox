package github.tornaco.android.thanos.core.os;

import github.tornaco.android.thanos.core.os.RR;

interface ICallback {
    oneway void onRes(in RR res);
}