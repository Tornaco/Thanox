package github.tornaco.android.thanos.core;

import github.tornaco.android.thanos.core.IThanos;


import android.content.IntentFilter;

interface IThanosProvider {
    IThanos getThanos();
}