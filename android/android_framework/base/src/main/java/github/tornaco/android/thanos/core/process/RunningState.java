/*
 * Copyright (C) 2019 The Android Open Source Project
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
 */

package github.tornaco.android.thanos.core.process;

import android.app.ActivityManager;
import android.app.ActivityThread;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.content.pm.UserInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.UserHandle;
import android.text.format.Formatter;
import android.util.Log;
import android.util.SparseArray;

import com.elvishew.xlog.XLog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AppInfo;
import util.PinyinComparatorUtils;

/**
 * Singleton for retrieving and monitoring the state about all running
 * applications/processes/services.
 */
public class RunningState {
    static final String TAG = "RunningState";
    static final boolean DEBUG_COMPARE = false;

    static Object sGlobalLock = new Object();
    static RunningState sInstance;

    static final int MAX_SERVICES = 100;

    final Context mApplicationContext;
    final ActivityManager mAm;
    final PackageManager mPm;
    final int mMyUserId;

    // Processes that are hosting a service we are interested in, organized
    // by uid and name.  Note that this mapping does not change even across
    // service restarts, and during a restart there will still be a process
    // entry.
    final SparseArray<HashMap<String, ProcessItem>> mServiceProcessesByName
            = new SparseArray<HashMap<String, ProcessItem>>();

    // Processes that are hosting a service we are interested in, organized
    // by their pid.  These disappear and re-appear as services are restarted.
    final SparseArray<ProcessItem> mServiceProcessesByPid
            = new SparseArray<ProcessItem>();

    // Used to sort the interesting processes.
    final ServiceProcessComparator mServiceProcessComparator
            = new ServiceProcessComparator();

    // Additional interesting processes to be shown to the user, even if
    // there is no service running in them.
    final ArrayList<ProcessItem> mInterestingProcesses = new ArrayList<ProcessItem>();

    // All currently running processes, for finding dependencies etc.
    final SparseArray<ProcessItem> mRunningProcesses
            = new SparseArray<ProcessItem>();

    // The processes associated with services, in sorted order.
    final ArrayList<ProcessItem> mProcessItems = new ArrayList<ProcessItem>();

    // All processes, used for retrieving memory information.
    final ArrayList<ProcessItem> mAllProcessItems = new ArrayList<ProcessItem>();

    // If there are other users on the device, these are the merged items
    // representing all items that would be put in mMergedItems for that user.
    final SparseArray<MergedItem> mOtherUserMergedItems = new SparseArray<MergedItem>();

    // If there are other users on the device, these are the merged items
    // representing all items that would be put in mUserBackgroundItems for that user.
    final SparseArray<MergedItem> mOtherUserBackgroundItems = new SparseArray<MergedItem>();

    static class AppProcessInfo {
        final ActivityManager.RunningAppProcessInfo info;
        boolean hasServices;
        boolean hasForegroundServices;

        AppProcessInfo(ActivityManager.RunningAppProcessInfo _info) {
            info = _info;
        }
    }

    // Temporary structure used when updating above information.
    final SparseArray<AppProcessInfo> mTmpAppProcesses = new SparseArray<AppProcessInfo>();

    int mSequence = 0;

    public final Comparator<MergedItem> mBackgroundComparator
            = new Comparator<MergedItem>() {
        @Override
        public int compare(MergedItem lhs, MergedItem rhs) {
            if (DEBUG_COMPARE) {
                Log.i(TAG, "Comparing " + lhs + " with " + rhs);
                Log.i(TAG, "     Proc " + lhs.mProcess + " with " + rhs.mProcess);
                Log.i(TAG, "   UserId " + lhs.mUserId + " with " + rhs.mUserId);
            }
            if (lhs.mUserId != rhs.mUserId) {
                if (lhs.mUserId == mMyUserId) return -1;
                if (rhs.mUserId == mMyUserId) return 1;
                return lhs.mUserId < rhs.mUserId ? -1 : 1;
            }
            if (lhs.mProcess == rhs.mProcess) {
                if (lhs.mLabel == rhs.mLabel) {
                    return 0;
                }
                return lhs.mLabel != null ? lhs.mLabel.compareTo(rhs.mLabel) : -1;
            }
            if (lhs.mProcess == null) return -1;
            if (rhs.mProcess == null) return 1;
            if (DEBUG_COMPARE) Log.i(TAG, "    Label " + lhs.mProcess.mLabel
                    + " with " + rhs.mProcess.mLabel);
            final ActivityManager.RunningAppProcessInfo lhsInfo
                    = lhs.mProcess.mRunningProcessInfo;
            final ActivityManager.RunningAppProcessInfo rhsInfo
                    = rhs.mProcess.mRunningProcessInfo;
            final boolean lhsBg = lhsInfo.importance
                    >= ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND;
            final boolean rhsBg = rhsInfo.importance
                    >= ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND;
            if (DEBUG_COMPARE) Log.i(TAG, "       Bg " + lhsBg + " with " + rhsBg);
            if (lhsBg != rhsBg) {
                return lhsBg ? 1 : -1;
            }
            final boolean lhsA = (lhsInfo.flags
                    & ActivityManager.RunningAppProcessInfo.FLAG_HAS_ACTIVITIES) != 0;
            final boolean rhsA = (rhsInfo.flags
                    & ActivityManager.RunningAppProcessInfo.FLAG_HAS_ACTIVITIES) != 0;
            if (DEBUG_COMPARE) Log.i(TAG, "      Act " + lhsA + " with " + rhsA);
            if (lhsA != rhsA) {
                return lhsA ? -1 : 1;
            }
            if (DEBUG_COMPARE) Log.i(TAG, "      Lru " + lhsInfo.lru + " with " + rhsInfo.lru);
            if (lhsInfo.lru != rhsInfo.lru) {
                return lhsInfo.lru < rhsInfo.lru ? -1 : 1;
            }
            if (lhs.mProcess.mLabel == rhs.mProcess.mLabel) {
                return 0;
            }
            if (lhs.mProcess.mLabel == null) return 1;
            if (rhs.mProcess.mLabel == null) return -1;
            return lhs.mProcess.mLabel.compareTo(rhs.mProcess.mLabel);
        }
    };

    // ----- following protected by mLock -----

    ArrayList<BaseItem> mItems = new ArrayList<BaseItem>();
    ArrayList<MergedItem> mMergedItems = new ArrayList<MergedItem>();
    ArrayList<MergedItem> mBackgroundItems = new ArrayList<MergedItem>();
    ArrayList<MergedItem> mUserBackgroundItems = new ArrayList<MergedItem>();

    int mNumBackgroundProcesses;
    public long mBackgroundProcessMemory;
    int mNumForegroundProcesses;
    long mForegroundProcessMemory;
    int mNumServiceProcesses;
    public long mServiceProcessMemory;

    public static class UserState {
        public UserInfo mInfo;
        public String mLabel;
        public Drawable mIcon;
    }

    public static class BaseItem {
        public final boolean mIsProcess;
        public final int mUserId;

        public PackageItemInfo mPackageInfo;
        public CharSequence mDisplayLabel;
        public String mLabel;
        public String mDescription;

        public int mCurSeq;

        public long mActiveSince;
        public long mSize;
        public String mSizeStr;
        public String mCurSizeStr;
        public boolean mNeedDivider;
        public boolean mBackground;

        public BaseItem(boolean isProcess, int userId) {
            mIsProcess = isProcess;
            mUserId = userId;
        }
    }

    public static class ServiceItem extends BaseItem {
        public ActivityManager.RunningServiceInfo mRunningService;
        public ServiceInfo mServiceInfo;
        boolean mShownAsStarted;

        MergedItem mMergedItem;

        public ServiceItem(int userId) {
            super(false, userId);
        }
    }

    public static class ProcessItem extends BaseItem {
        final HashMap<ComponentName, ServiceItem> mServices
                = new HashMap<ComponentName, ServiceItem>();
        final SparseArray<ProcessItem> mDependentProcesses
                = new SparseArray<ProcessItem>();

        public final int mUid;
        public final String mProcessName;
        public int mPid;

        ProcessItem mClient;
        int mLastNumDependentProcesses;

        int mRunningSeq;
        public ActivityManager.RunningAppProcessInfo mRunningProcessInfo;

        MergedItem mMergedItem;

        boolean mInteresting;

        // Purely for sorting.
        boolean mIsSystem;
        boolean mIsStarted;
        long mActiveSince;

        ThanosManager thanosManager;

        public ProcessItem(Context context, int uid, String processName) {
            super(true, UserHandle.getUserId(uid));
            mDescription = "service_process_name" + processName;
            mUid = uid;
            mProcessName = processName;
            thanosManager = ThanosManager.from(context);
        }

        public void ensureLabel(PackageManager pm) {
            if (mLabel != null) {
                return;
            }

            try {
                ApplicationInfo ai = pm.getApplicationInfo(mProcessName, 0);
                if (ai.uid == mUid) {
                    mDisplayLabel = ai.loadLabel(pm);
                    mLabel = mDisplayLabel.toString();
                    mPackageInfo = ai;
                    return;
                }
            } catch (PackageManager.NameNotFoundException e) {
            }

            // If we couldn't get information about the overall
            // process, try to find something about the uid.
            String[] pkgs = thanosManager.getPkgManager().getPackagesForUid(mUid);

            // If there is one package with this uid, that is what we want.
            if (pkgs.length == 1) {
                try {
                    ApplicationInfo ai = pm.getApplicationInfo(pkgs[0], 0);
                    mDisplayLabel = ai.loadLabel(pm);
                    mLabel = mDisplayLabel.toString();
                    mPackageInfo = ai;
                    return;
                } catch (PackageManager.NameNotFoundException e) {
                }
            }

            // If there are multiple, see if one gives us the official name
            // for this uid.
            for (String name : pkgs) {
                try {
                    PackageInfo pi = pm.getPackageInfo(name, 0);
                    if (pi.sharedUserLabel != 0) {
                        CharSequence nm = pm.getText(name,
                                pi.sharedUserLabel, pi.applicationInfo);
                        if (nm != null) {
                            mDisplayLabel = nm;
                            mLabel = nm.toString();
                            mPackageInfo = pi.applicationInfo;
                            return;
                        }
                    }
                } catch (PackageManager.NameNotFoundException e) {
                }
            }

            // If still don't have anything to display, just use the
            // service info.
            if (mServices.size() > 0) {
                ApplicationInfo ai = mServices.values().iterator().next()
                        .mServiceInfo.applicationInfo;
                mPackageInfo = ai;
                mDisplayLabel = mPackageInfo.loadLabel(pm);
                mLabel = mDisplayLabel.toString();
                return;
            }

            // Finally... whatever, just pick the first package's name.
            try {
                ApplicationInfo ai = pm.getApplicationInfo(pkgs[0], 0);
                mDisplayLabel = ai.loadLabel(pm);
                mLabel = mDisplayLabel.toString();
                mPackageInfo = ai;
                return;
            } catch (PackageManager.NameNotFoundException e) {
            }
        }

        boolean updateService(Context context, ActivityManager.RunningServiceInfo service) {
            final PackageManager pm = context.getPackageManager();

            boolean changed = false;
            ServiceItem si = mServices.get(service.service);
            if (si == null) {
                changed = true;
                si = new ServiceItem(mUserId);
                si.mRunningService = service;
                try {
                    si.mServiceInfo = ActivityThread.getPackageManager().getServiceInfo(
                            service.service, 0,
                            UserHandle.getUserId(service.uid));

                    if (si.mServiceInfo == null) {
                        Log.d("RunningService", "getServiceInfo returned null for: "
                                + service.service);
                        return false;
                    }
                } catch (Throwable e) {
                    Log.d("RunningService", "getServiceInfo error for: "
                            + Log.getStackTraceString(e));
                    return false;
                }
                si.mDisplayLabel = makeLabel(pm,
                        si.mRunningService.service.getClassName(), si.mServiceInfo);
                mLabel = mDisplayLabel != null ? mDisplayLabel.toString() : null;
                si.mPackageInfo = si.mServiceInfo.applicationInfo;
                mServices.put(service.service, si);
            }
            si.mCurSeq = mCurSeq;
            si.mRunningService = service;
            long activeSince = service.restarting == 0 ? service.activeSince : -1;
            if (si.mActiveSince != activeSince) {
                si.mActiveSince = activeSince;
                changed = true;
            }
            if (service.clientPackage != null && service.clientLabel != 0) {
                if (si.mShownAsStarted) {
                    si.mShownAsStarted = false;
                    changed = true;
                }
                try {
                    Resources clientr = pm.getResourcesForApplication(service.clientPackage);
                    String label = clientr.getString(service.clientLabel);
                    si.mDescription = label;
                } catch (PackageManager.NameNotFoundException e) {
                    si.mDescription = null;
                }
            } else {
                if (!si.mShownAsStarted) {
                    si.mShownAsStarted = true;
                    changed = true;
                }
                si.mDescription = "STARTED BY APP";
            }

            return changed;
        }

        boolean updateSize(Context context, long pss, int curSeq) {
            mSize = pss * 1024;
            if (mCurSeq == curSeq) {
                String sizeStr = Formatter.formatShortFileSize(
                        context, mSize);
                if (!sizeStr.equals(mSizeStr)) {
                    mSizeStr = sizeStr;
                    // We update this on the second tick where we update just
                    // the text in the current items, so no need to say we
                    // changed here.
                    return false;
                }
            }
            return false;
        }

        boolean buildDependencyChain(Context context, PackageManager pm, int curSeq) {
            final int NP = mDependentProcesses.size();
            boolean changed = false;
            for (int i = 0; i < NP; i++) {
                ProcessItem proc = mDependentProcesses.valueAt(i);
                if (proc.mClient != this) {
                    changed = true;
                    proc.mClient = this;
                }
                proc.mCurSeq = curSeq;
                proc.ensureLabel(pm);
                changed |= proc.buildDependencyChain(context, pm, curSeq);
            }

            if (mLastNumDependentProcesses != mDependentProcesses.size()) {
                changed = true;
                mLastNumDependentProcesses = mDependentProcesses.size();
            }

            return changed;
        }

        void addDependentProcesses(ArrayList<BaseItem> dest,
                                   ArrayList<ProcessItem> destProc) {
            final int NP = mDependentProcesses.size();
            for (int i = 0; i < NP; i++) {
                ProcessItem proc = mDependentProcesses.valueAt(i);
                proc.addDependentProcesses(dest, destProc);
                dest.add(proc);
                if (proc.mPid > 0) {
                    destProc.add(proc);
                }
            }
        }
    }

    public static class MergedItem extends BaseItem implements Comparable<MergedItem> {
        public ProcessItem mProcess;
        public UserState mUser;
        public final ArrayList<ProcessItem> mOtherProcesses = new ArrayList<ProcessItem>();
        public final ArrayList<ServiceItem> mServices = new ArrayList<ServiceItem>();
        public final ArrayList<MergedItem> mChildren = new ArrayList<MergedItem>();
        public AppInfo appInfo;

        public int mLastNumProcesses = -1, mLastNumServices = -1;

        MergedItem(int userId) {
            super(false, userId);
        }

        private void setDescription(Context context, int numProcesses, int numServices) {
            if (mLastNumProcesses != numProcesses || mLastNumServices != numServices) {
                mLastNumProcesses = numProcesses;
                mLastNumServices = numServices;
                mDescription = "???" + numProcesses + numServices;
            }
        }

        boolean update(Context context, boolean background) {
            mBackground = background;

            if (mUser != null) {
                // This is a merged item that contains a child collection
                // of items...  that is, it is an entire user, containing
                // everything associated with that user.  So set it up as such.
                // For concrete stuff we need about the process of this item,
                // we will just use the info from the first child.
                MergedItem child0 = mChildren.get(0);
                mPackageInfo = child0.mProcess.mPackageInfo;
                mLabel = mUser != null ? mUser.mLabel : null;
                mDisplayLabel = mLabel;
                int numProcesses = 0;
                int numServices = 0;
                mActiveSince = -1;
                for (int i = 0; i < mChildren.size(); i++) {
                    MergedItem child = mChildren.get(i);
                    numProcesses += child.mLastNumProcesses;
                    numServices += child.mLastNumServices;
                    if (child.mActiveSince >= 0 && mActiveSince < child.mActiveSince) {
                        mActiveSince = child.mActiveSince;
                    }
                }
                if (!mBackground) {
                    setDescription(context, numProcesses, numServices);
                }
            } else {
                mPackageInfo = mProcess.mPackageInfo;
                mDisplayLabel = mProcess.mDisplayLabel;
                mLabel = mProcess.mLabel;

                if (!mBackground) {
                    setDescription(context, (mProcess.mPid > 0 ? 1 : 0) + mOtherProcesses.size(),
                            mServices.size());
                }

                mActiveSince = -1;
                for (int i = 0; i < mServices.size(); i++) {
                    ServiceItem si = mServices.get(i);
                    if (si.mActiveSince >= 0 && mActiveSince < si.mActiveSince) {
                        mActiveSince = si.mActiveSince;
                    }
                }
            }
            return false;
        }

        boolean updateSize(Context context) {
            if (mUser != null) {
                mSize = 0;
                for (int i = 0; i < mChildren.size(); i++) {
                    MergedItem child = mChildren.get(i);
                    child.updateSize(context);
                    mSize += child.mSize;
                }
            } else {
                mSize = mProcess.mSize;
                for (int i = 0; i < mOtherProcesses.size(); i++) {
                    mSize += mOtherProcesses.get(i).mSize;
                }
            }

            String sizeStr = Formatter.formatShortFileSize(
                    context, mSize);
            if (!sizeStr.equals(mSizeStr)) {
                mSizeStr = sizeStr;
                // We update this on the second tick where we update just
                // the text in the current items, so no need to say we
                // changed here.
                return false;
            }
            return false;
        }

        @Override
        public int compareTo(@NotNull MergedItem mergedItem) {
            return PinyinComparatorUtils.compare(mDisplayLabel, mergedItem.mDisplayLabel);
        }
    }

    class ServiceProcessComparator implements Comparator<ProcessItem> {
        public int compare(ProcessItem object1, ProcessItem object2) {
            if (object1.mUserId != object2.mUserId) {
                if (object1.mUserId == mMyUserId) return -1;
                if (object2.mUserId == mMyUserId) return 1;
                return object1.mUserId < object2.mUserId ? -1 : 1;
            }
            if (object1.mIsStarted != object2.mIsStarted) {
                // Non-started processes go last.
                return object1.mIsStarted ? -1 : 1;
            }
            if (object1.mIsSystem != object2.mIsSystem) {
                // System processes go below non-system.
                return object1.mIsSystem ? 1 : -1;
            }
            if (object1.mActiveSince != object2.mActiveSince) {
                // Remaining ones are sorted with the longest running
                // services last.
                return (object1.mActiveSince > object2.mActiveSince) ? -1 : 1;
            }
            return 0;
        }
    }

    public static CharSequence makeLabel(PackageManager pm,
                                         String className, PackageItemInfo item) {
        if (item != null && (item.labelRes != 0
                || item.nonLocalizedLabel != null)) {
            CharSequence label = item.loadLabel(pm);
            if (label != null) {
                return label;
            }
        }

        String label = className;
        int tail = label.lastIndexOf('.');
        if (tail >= 0) {
            label = label.substring(tail + 1, label.length());
        }
        return label;
    }

    public static RunningState getInstance(Context context) {
        synchronized (sGlobalLock) {
            if (sInstance == null) {
                sInstance = new RunningState(context);
            }
            return sInstance;
        }
    }

    private RunningState(Context context) {
        mApplicationContext = context.getApplicationContext();
        mAm = (ActivityManager) mApplicationContext.getSystemService(Context.ACTIVITY_SERVICE);
        mPm = mApplicationContext.getPackageManager();
        mMyUserId = UserHandle.myUserId();
    }


    public void updateNow() {
        try {
            update(mApplicationContext);
        } catch (Throwable e) {
            XLog.e("Error updateNow: %s", Log.getStackTraceString(e));
        }
    }

    private boolean isInterestingProcess(ActivityManager.RunningAppProcessInfo pi) {
        if ((pi.flags & ActivityManager.RunningAppProcessInfo.FLAG_CANT_SAVE_STATE) != 0) {
            return true;
        }
        if ((pi.flags & ActivityManager.RunningAppProcessInfo.FLAG_PERSISTENT) == 0
                && pi.importance >= ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                && pi.importance < ActivityManager.RunningAppProcessInfo.IMPORTANCE_CANT_SAVE_STATE
                && pi.importanceReasonCode
                == ActivityManager.RunningAppProcessInfo.REASON_UNKNOWN) {
            return true;
        }
        return false;
    }

    private void reset() {
        mServiceProcessesByName.clear();
        mServiceProcessesByPid.clear();
        mInterestingProcesses.clear();
        mRunningProcesses.clear();
        mProcessItems.clear();
        mAllProcessItems.clear();
    }

    private boolean update(Context context) {
        final PackageManager pm = context.getPackageManager();

        mSequence++;

        boolean changed = false;

        // Retrieve list of services, filtering out anything that definitely
        // won't be shown in the UI.
        List<ActivityManager.RunningServiceInfo> services
                = ThanosManager.from(context).getActivityManager().getRunningServiceLegacy(MAX_SERVICES);
        int NS = services != null ? services.size() : 0;
        for (int i = 0; i < NS; i++) {
            ActivityManager.RunningServiceInfo si = services.get(i);
            // We are not interested in services that have not been started
            // and don't have a known client, because
            // there is nothing the user can do about them.
            if (!si.started && si.clientLabel == 0) {
                services.remove(i);
                i--;
                NS--;
                continue;
            }
            // We likewise don't care about services running in a
            // persistent process like the system or phone.
            if ((si.flags & ActivityManager.RunningServiceInfo.FLAG_PERSISTENT_PROCESS)
                    != 0) {
                services.remove(i);
                i--;
                NS--;
                continue;
            }
        }

        // Retrieve list of running processes, organizing them into a sparse
        // array for easy retrieval.
        List<ActivityManager.RunningAppProcessInfo> processes
                = ThanosManager.from(context).getActivityManager().getRunningAppProcessLegacy();
        final int NP = processes != null ? processes.size() : 0;
        mTmpAppProcesses.clear();
        for (int i = 0; i < NP; i++) {
            ActivityManager.RunningAppProcessInfo pi = processes.get(i);
            mTmpAppProcesses.put(pi.pid, new AppProcessInfo(pi));
        }

        // Initial iteration through running services to collect per-process
        // info about them.
        for (int i = 0; i < NS; i++) {
            ActivityManager.RunningServiceInfo si = services.get(i);
            if (si.restarting == 0 && si.pid > 0) {
                AppProcessInfo ainfo = mTmpAppProcesses.get(si.pid);
                if (ainfo != null) {
                    ainfo.hasServices = true;
                    if (si.foreground) {
                        ainfo.hasForegroundServices = true;
                    }
                }
            }
        }

        // Update state we are maintaining about process that are running services.
        for (int i = 0; i < NS; i++) {
            ActivityManager.RunningServiceInfo si = services.get(i);

            // If this service's process is in use at a higher importance
            // due to another process bound to one of its services, then we
            // won't put it in the top-level list of services.  Instead we
            // want it to be included in the set of processes that the other
            // process needs.
            if (si.restarting == 0 && si.pid > 0) {
                AppProcessInfo ainfo = mTmpAppProcesses.get(si.pid);
                if (ainfo != null && !ainfo.hasForegroundServices) {
                    // This process does not have any foreground services.
                    // If its importance is greater than the service importance
                    // then there is something else more significant that is
                    // keeping it around that it should possibly be included as
                    // a part of instead of being shown by itself.
                    if (ainfo.info.importance
                            < ActivityManager.RunningAppProcessInfo.IMPORTANCE_SERVICE) {
                        // Follow process chain to see if there is something
                        // else that could be shown
                        boolean skip = false;
                        ainfo = mTmpAppProcesses.get(ainfo.info.importanceReasonPid);
                        while (ainfo != null) {
                            if (ainfo.hasServices || isInterestingProcess(ainfo.info)) {
                                skip = true;
                                break;
                            }
                            ainfo = mTmpAppProcesses.get(ainfo.info.importanceReasonPid);
                        }
                        if (skip) {
                            continue;
                        }
                    }
                }
            }

            HashMap<String, ProcessItem> procs = mServiceProcessesByName.get(si.uid);
            if (procs == null) {
                procs = new HashMap<String, ProcessItem>();
                mServiceProcessesByName.put(si.uid, procs);
            }
            ProcessItem proc = procs.get(si.process);
            if (proc == null) {
                changed = true;
                proc = new ProcessItem(context, si.uid, si.process);
                procs.put(si.process, proc);
            }

            if (proc.mCurSeq != mSequence) {
                int pid = si.restarting == 0 ? si.pid : 0;
                if (pid != proc.mPid) {
                    changed = true;
                    if (proc.mPid != pid) {
                        if (proc.mPid != 0) {
                            mServiceProcessesByPid.remove(proc.mPid);
                        }
                        if (pid != 0) {
                            mServiceProcessesByPid.put(pid, proc);
                        }
                        proc.mPid = pid;
                    }
                }
                proc.mDependentProcesses.clear();
                proc.mCurSeq = mSequence;
            }
            changed |= proc.updateService(context, si);
        }

        // Now update the map of other processes that are running (but
        // don't have services actively running inside them).
        for (int i = 0; i < NP; i++) {
            ActivityManager.RunningAppProcessInfo pi = processes.get(i);
            ProcessItem proc = mServiceProcessesByPid.get(pi.pid);
            if (proc == null) {
                // This process is not one that is a direct container
                // of a service, so look for it in the secondary
                // running list.
                proc = mRunningProcesses.get(pi.pid);
                if (proc == null) {
                    changed = true;
                    proc = new ProcessItem(context, pi.uid, pi.processName);
                    proc.mPid = pi.pid;
                    mRunningProcesses.put(pi.pid, proc);
                }
                proc.mDependentProcesses.clear();
            }

            if (isInterestingProcess(pi)) {
                if (!mInterestingProcesses.contains(proc)) {
                    changed = true;
                    mInterestingProcesses.add(proc);
                }
                proc.mCurSeq = mSequence;
                proc.mInteresting = true;
                proc.ensureLabel(pm);
            } else {
                proc.mInteresting = false;
            }

            proc.mRunningSeq = mSequence;
            proc.mRunningProcessInfo = pi;
        }

        // Build the chains from client processes to the process they are
        // dependent on; also remove any old running processes.
        int NRP = mRunningProcesses.size();
        for (int i = 0; i < NRP; ) {
            ProcessItem proc = mRunningProcesses.valueAt(i);
            if (proc.mRunningSeq == mSequence) {
                int clientPid = proc.mRunningProcessInfo.importanceReasonPid;
                if (clientPid != 0) {
                    ProcessItem client = mServiceProcessesByPid.get(clientPid);
                    if (client == null) {
                        client = mRunningProcesses.get(clientPid);
                    }
                    if (client != null) {
                        client.mDependentProcesses.put(proc.mPid, proc);
                    }
                } else {
                    // In this pass the process doesn't have a client.
                    // Clear to make sure that, if it later gets the same one,
                    // we will detect the change.
                    proc.mClient = null;
                }
                i++;
            } else {
                changed = true;
                mRunningProcesses.remove(mRunningProcesses.keyAt(i));
                NRP--;
            }
        }

        // Remove any old interesting processes.
        int NHP = mInterestingProcesses.size();
        for (int i = 0; i < NHP; i++) {
            ProcessItem proc = mInterestingProcesses.get(i);
            if (!proc.mInteresting || mRunningProcesses.get(proc.mPid) == null) {
                changed = true;
                mInterestingProcesses.remove(i);
                i--;
                NHP--;
            }
        }

        // Follow the tree from all primary service processes to all
        // processes they are dependent on, marking these processes as
        // still being active and determining if anything has changed.
        final int NAP = mServiceProcessesByPid.size();
        for (int i = 0; i < NAP; i++) {
            ProcessItem proc = mServiceProcessesByPid.valueAt(i);
            if (proc.mCurSeq == mSequence) {
                changed |= proc.buildDependencyChain(context, pm, mSequence);
            }
        }

        // Look for services and their primary processes that no longer exist...
        ArrayList<Integer> uidToDelete = null;
        for (int i = 0; i < mServiceProcessesByName.size(); i++) {
            HashMap<String, ProcessItem> procs = mServiceProcessesByName.valueAt(i);
            Iterator<ProcessItem> pit = procs.values().iterator();
            while (pit.hasNext()) {
                ProcessItem pi = pit.next();
                if (pi.mCurSeq == mSequence) {
                    pi.ensureLabel(pm);
                    if (pi.mPid == 0) {
                        // Sanity: a non-process can't be dependent on
                        // anything.
                        pi.mDependentProcesses.clear();
                    }
                } else {
                    changed = true;
                    pit.remove();
                    if (procs.size() == 0) {
                        if (uidToDelete == null) {
                            uidToDelete = new ArrayList<Integer>();
                        }
                        uidToDelete.add(mServiceProcessesByName.keyAt(i));
                    }
                    if (pi.mPid != 0) {
                        mServiceProcessesByPid.remove(pi.mPid);
                    }
                    continue;
                }
                Iterator<ServiceItem> sit = pi.mServices.values().iterator();
                while (sit.hasNext()) {
                    ServiceItem si = sit.next();
                    if (si.mCurSeq != mSequence) {
                        changed = true;
                        sit.remove();
                    }
                }
            }
        }

        if (uidToDelete != null) {
            for (int i = 0; i < uidToDelete.size(); i++) {
                int uid = uidToDelete.get(i);
                mServiceProcessesByName.remove(uid);
            }
        }

        if (changed) {
            // First determine an order for the services.
            ArrayList<ProcessItem> sortedProcesses = new ArrayList<ProcessItem>();
            for (int i = 0; i < mServiceProcessesByName.size(); i++) {
                for (ProcessItem pi : mServiceProcessesByName.valueAt(i).values()) {
                    pi.mIsSystem = false;
                    pi.mIsStarted = true;
                    pi.mActiveSince = Long.MAX_VALUE;
                    for (ServiceItem si : pi.mServices.values()) {
                        if (si.mServiceInfo != null
                                && (si.mServiceInfo.applicationInfo.flags
                                & ApplicationInfo.FLAG_SYSTEM) != 0) {
                            pi.mIsSystem = true;
                        }
                        if (si.mRunningService != null
                                && si.mRunningService.clientLabel != 0) {
                            pi.mIsStarted = false;
                            if (pi.mActiveSince > si.mRunningService.activeSince) {
                                pi.mActiveSince = si.mRunningService.activeSince;
                            }
                        }
                    }
                    sortedProcesses.add(pi);
                }
            }

            Collections.sort(sortedProcesses, mServiceProcessComparator);

            ArrayList<BaseItem> newItems = new ArrayList<BaseItem>();
            ArrayList<MergedItem> newMergedItems = new ArrayList<MergedItem>();
            mProcessItems.clear();
            for (int i = 0; i < sortedProcesses.size(); i++) {
                ProcessItem pi = sortedProcesses.get(i);
                pi.mNeedDivider = false;

                int firstProc = mProcessItems.size();
                // First add processes we are dependent on.
                pi.addDependentProcesses(newItems, mProcessItems);
                // And add the process itself.
                newItems.add(pi);
                if (pi.mPid > 0) {
                    mProcessItems.add(pi);
                }

                // Now add the services running in it.
                MergedItem mergedItem = null;
                boolean haveAllMerged = false;
                boolean needDivider = false;
                for (ServiceItem si : pi.mServices.values()) {
                    si.mNeedDivider = needDivider;
                    needDivider = true;
                    newItems.add(si);
                    if (si.mMergedItem != null) {
                        if (mergedItem != null && mergedItem != si.mMergedItem) {
                            haveAllMerged = false;
                        }
                        mergedItem = si.mMergedItem;
                    } else {
                        haveAllMerged = false;
                    }
                }

                if (!haveAllMerged || mergedItem == null
                        || mergedItem.mServices.size() != pi.mServices.size()) {
                    // Whoops, we need to build a new MergedItem!
                    mergedItem = new MergedItem(pi.mUserId);
                    for (ServiceItem si : pi.mServices.values()) {
                        mergedItem.mServices.add(si);
                        si.mMergedItem = mergedItem;
                    }
                    mergedItem.mProcess = pi;
                    mergedItem.mOtherProcesses.clear();
                    for (int mpi = firstProc; mpi < (mProcessItems.size() - 1); mpi++) {
                        mergedItem.mOtherProcesses.add(mProcessItems.get(mpi));
                    }
                }

                mergedItem.update(context, false);
                if (mergedItem.mUserId != mMyUserId) {
                } else {
                    newMergedItems.add(mergedItem);
                }
            }

            // Finally, interesting processes need to be shown and will
            // go at the top.
            NHP = mInterestingProcesses.size();
            for (int i = 0; i < NHP; i++) {
                ProcessItem proc = mInterestingProcesses.get(i);
                if (proc.mClient == null && proc.mServices.size() <= 0) {
                    if (proc.mMergedItem == null) {
                        proc.mMergedItem = new MergedItem(proc.mUserId);
                        proc.mMergedItem.mProcess = proc;
                    }
                    proc.mMergedItem.update(context, false);
                    if (proc.mMergedItem.mUserId != mMyUserId) {

                    } else {
                        newMergedItems.add(0, proc.mMergedItem);
                    }
                    mProcessItems.add(proc);
                }
            }

            // Finally finally, user aggregated merged items need to be
            // updated now that they have all of their children.
            final int NU = mOtherUserMergedItems.size();
            for (int i = 0; i < NU; i++) {
                MergedItem user = mOtherUserMergedItems.valueAt(i);
                if (user.mCurSeq == mSequence) {
                    user.update(context, false);
                }
            }

            mItems = newItems;
            mMergedItems = newMergedItems;
        }

        // Count number of interesting other (non-active) processes, and
        // build a list of all processes we will retrieve memory for.
        mAllProcessItems.clear();
        mAllProcessItems.addAll(mProcessItems);
        int numBackgroundProcesses = 0;
        int numForegroundProcesses = 0;
        int numServiceProcesses = 0;
        NRP = mRunningProcesses.size();
        for (int i = 0; i < NRP; i++) {
            ProcessItem proc = mRunningProcesses.valueAt(i);
            if (proc.mCurSeq != mSequence) {
                // We didn't hit this process as a dependency on one
                // of our active ones, so add it up if needed.
                if (proc.mRunningProcessInfo.importance >=
                        ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    numBackgroundProcesses++;
                    mAllProcessItems.add(proc);
                } else if (proc.mRunningProcessInfo.importance <=
                        ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
                    numForegroundProcesses++;
                    mAllProcessItems.add(proc);
                } else {
                    Log.i("RunningState", "Unknown non-service process: "
                            + proc.mProcessName + " #" + proc.mPid);
                }
            } else {
                numServiceProcesses++;
            }
        }

        long backgroundProcessMemory = 0;
        long foregroundProcessMemory = 0;
        long serviceProcessMemory = 0;
        ArrayList<MergedItem> newBackgroundItems = null;
        ArrayList<MergedItem> newUserBackgroundItems = null;
        boolean diffUsers = false;
        try {
            final int numProc = mAllProcessItems.size();
            int[] pids = new int[numProc];
            for (int i = 0; i < numProc; i++) {
                pids[i] = mAllProcessItems.get(i).mPid;
            }
            long[] pss = ThanosManager.from(context).getActivityManager().getProcessPss(pids);
            int bgIndex = 0;
            for (int i = 0; i < pids.length; i++) {
                ProcessItem proc = mAllProcessItems.get(i);
                changed |= proc.updateSize(context, pss[i], mSequence);
                if (proc.mCurSeq == mSequence) {
                    serviceProcessMemory += proc.mSize;
                } else if (proc.mRunningProcessInfo.importance >=
                        ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    backgroundProcessMemory += proc.mSize;
                    MergedItem mergedItem;
                    if (newBackgroundItems != null) {
                        mergedItem = proc.mMergedItem = new MergedItem(proc.mUserId);
                        proc.mMergedItem.mProcess = proc;
                        diffUsers |= mergedItem.mUserId != mMyUserId;
                        newBackgroundItems.add(mergedItem);
                    } else {
                        if (bgIndex >= mBackgroundItems.size()
                                || mBackgroundItems.get(bgIndex).mProcess != proc) {
                            newBackgroundItems = new ArrayList<MergedItem>(numBackgroundProcesses);
                            for (int bgi = 0; bgi < bgIndex; bgi++) {
                                mergedItem = mBackgroundItems.get(bgi);
                                diffUsers |= mergedItem.mUserId != mMyUserId;
                                newBackgroundItems.add(mergedItem);
                            }
                            mergedItem = proc.mMergedItem = new MergedItem(proc.mUserId);
                            proc.mMergedItem.mProcess = proc;
                            diffUsers |= mergedItem.mUserId != mMyUserId;
                            newBackgroundItems.add(mergedItem);
                        } else {
                            mergedItem = mBackgroundItems.get(bgIndex);
                        }
                    }
                    mergedItem.update(context, true);
                    mergedItem.updateSize(context);
                    bgIndex++;
                } else if (proc.mRunningProcessInfo.importance <=
                        ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
                    foregroundProcessMemory += proc.mSize;
                }
            }
        } catch (Throwable e) {
            XLog.e(Log.getStackTraceString(e));
        }

        if (newBackgroundItems == null) {
            // One or more at the bottom may no longer exist.
            if (mBackgroundItems.size() > numBackgroundProcesses) {
                newBackgroundItems = new ArrayList<MergedItem>(numBackgroundProcesses);
                for (int bgi = 0; bgi < numBackgroundProcesses; bgi++) {
                    MergedItem mergedItem = mBackgroundItems.get(bgi);
                    diffUsers |= mergedItem.mUserId != mMyUserId;
                    newBackgroundItems.add(mergedItem);
                }
            }
        }

        if (newBackgroundItems != null) {
            // The background items have changed; we need to re-build the
            // per-user items.
            if (!diffUsers) {
                // Easy: there are no other users, we can just use the same array.
                newUserBackgroundItems = newBackgroundItems;
            } else {
                // We now need to re-build the per-user list so that background
                // items for users are collapsed together.
                newUserBackgroundItems = new ArrayList<MergedItem>();
                final int NB = newBackgroundItems.size();
                for (int i = 0; i < NB; i++) {
                    MergedItem mergedItem = newBackgroundItems.get(i);
                    if (mergedItem.mUserId != mMyUserId) {
                    } else {
                        newUserBackgroundItems.add(mergedItem);
                    }
                }
                // And user aggregated merged items need to be
                // updated now that they have all of their children.
                final int NU = mOtherUserBackgroundItems.size();
                for (int i = 0; i < NU; i++) {
                    MergedItem user = mOtherUserBackgroundItems.valueAt(i);
                    if (user.mCurSeq == mSequence) {
                        user.update(context, true);
                        user.updateSize(context);
                    }
                }
            }
        }

        for (int i = 0; i < mMergedItems.size(); i++) {
            mMergedItems.get(i).updateSize(context);
        }

        mNumBackgroundProcesses = numBackgroundProcesses;
        mNumForegroundProcesses = numForegroundProcesses;
        mNumServiceProcesses = numServiceProcesses;
        mBackgroundProcessMemory = backgroundProcessMemory;
        mForegroundProcessMemory = foregroundProcessMemory;
        mServiceProcessMemory = serviceProcessMemory;
        if (newBackgroundItems != null) {
            mBackgroundItems = newBackgroundItems;
            mUserBackgroundItems = newUserBackgroundItems;
        }

        return changed;
    }


    public ArrayList<MergedItem> getCurrentMergedItems() {
        return mMergedItems;
    }

    public ArrayList<MergedItem> getCurrentBackgroundItems() {
        return mUserBackgroundItems;
    }
}
