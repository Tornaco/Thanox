/*
 * (C) Copyright 2022 Thanox
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package github.tornaco.thanos.android.noroot;


import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Handler;
import android.os.Process;

import androidx.annotation.Nullable;

import com.elvishew.xlog.XLog;

import github.tornaco.android.thanos.core.IThanos;
import github.tornaco.android.thanos.core.IThanosProvider;
import github.tornaco.android.thanos.core.app.ThanosManagerNative;
import github.tornaco.android.thanos.services.BootStrap;

public class ThanosProviderService extends IThanosProvider.Stub {
    public ThanosProviderService() {
    }

    @Override
    public IThanos getThanos() {
        if (ThanosManagerNative.getLocalService() == null) {
            installThanosService();
        }
        return ThanosManagerNative.getLocalService();
    }

    private void installThanosService() {
        XLog.w("installThanosService: " + " --- p" + Process.myPid() + " u" + Process.myUid());
        XLog.w("installThanosService, currentApplication: " + BootStrap.currentApplication());
        long ident = Binder.clearCallingIdentity();
        try {
            BootStrap.main("Shizuku", new String[0]);
            BootStrap.start(new ShellProcessContext(BootStrap.currentApplication()));
            BootStrap.ready();
        } catch (Throwable e) {
            XLog.e("********* ThanosProviderService#installThanosService *********", e);
        }
        Binder.restoreCallingIdentity(ident);
    }

    static class ShellProcessContext extends ContextWrapper {
        public ShellProcessContext(Context base) {
            super(base);
        }

        @Override
        public Intent registerReceiver(@Nullable BroadcastReceiver receiver, IntentFilter filter) {
            XLog.w("ShellProcessContext, drop registerReceiver call..." + receiver);
            return new Intent();
        }

        @Override
        public Intent registerReceiver(@Nullable BroadcastReceiver receiver, IntentFilter filter, int flags) {
            XLog.w("ShellProcessContext, drop registerReceiver call..." + receiver);
            return new Intent();
        }

        @Override
        public Intent registerReceiver(@Nullable BroadcastReceiver receiver, IntentFilter filter, @Nullable String broadcastPermission, @Nullable Handler scheduler) {
            XLog.w("ShellProcessContext, drop registerReceiver call..." + receiver);
            return new Intent();
        }

        @Override
        public Intent registerReceiver(@Nullable BroadcastReceiver receiver, IntentFilter filter, @Nullable String broadcastPermission, @Nullable Handler scheduler, int flags) {
            XLog.w("ShellProcessContext, drop registerReceiver call..." + receiver);
            return new Intent();
        }

        @Override
        public ContentResolver getContentResolver() {
            XLog.w("ShellProcessContext, drop getContentResolver call...");
            return null;
        }
    }
}