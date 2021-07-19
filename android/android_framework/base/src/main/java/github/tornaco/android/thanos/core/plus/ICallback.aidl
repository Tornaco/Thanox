package github.tornaco.android.thanos.core.plus;

import github.tornaco.android.thanos.core.plus.RR;

interface ICallback {
    oneway void onRes(in RR res);
}