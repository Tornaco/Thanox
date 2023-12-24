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

package now.fortuitous.thanos.process;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import android.text.BidiFormatter;
import android.text.format.DateUtils;
import android.text.format.Formatter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.RecyclerListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.process.RunningState;
import github.tornaco.android.thanos.core.util.MemInfoReader;
import github.tornaco.android.thanos.util.GlideApp;
import now.fortuitous.thanos.databinding.DataBindingAdapters;

public class RunningProcessesView extends FrameLayout
        implements AdapterView.OnItemClickListener, RecyclerListener {

    long SECONDARY_SERVER_MEM;

    final HashMap<View, ActiveItem> mActiveItems = new HashMap<View, ActiveItem>();

    ActivityManager mAm;

    RunningState mState;

    Fragment mOwner;

    Runnable mDataAvail;

    StringBuilder mBuilder = new StringBuilder(128);

    RunningState.BaseItem mCurSelected;

    ListView mListView;
    View mHeader;
    ServiceListAdapter mAdapter;
    LinearColorBar mColorBar;
    TextView mBackgroundProcessPrefix;
    TextView mAppsProcessPrefix;
    TextView mForegroundProcessPrefix;
    TextView mBackgroundProcessText;
    TextView mAppsProcessText;
    TextView mForegroundProcessText;

    long mCurTotalRam = -1;
    long mCurHighRam = -1;      // "System" or "Used"
    long mCurMedRam = -1;       // "Apps" or "Cached"
    long mCurLowRam = -1;       // "Free"
    boolean mCurShowCached = false;

    Dialog mCurDialog;

    MemInfoReader mMemInfoReader = new MemInfoReader();

    public static class ActiveItem {
        View mRootView;
        RunningState.BaseItem mItem;
        ActivityManager.RunningServiceInfo mService;
        ViewHolder mHolder;
        long mFirstRunTime;
        boolean mSetBackground;

        void updateTime(Context context, StringBuilder builder) {
            TextView uptimeView = null;

            if (mItem instanceof RunningState.ServiceItem) {
                // If we are displaying a service, then the service
                // uptime goes at the top.
                uptimeView = mHolder.size;

            } else {
                String size = mItem.mSizeStr != null ? mItem.mSizeStr : "";
                if (!size.equals(mItem.mCurSizeStr)) {
                    mItem.mCurSizeStr = size;
                    mHolder.size.setText(size);
                }

                if (mItem.mBackground) {
                    // This is a background process; no uptime.
                    if (!mSetBackground) {
                        mSetBackground = true;
                        mHolder.uptime.setText("");
                    }
                } else if (mItem instanceof RunningState.MergedItem) {
                    // This item represents both services and processes,
                    // so show the service uptime below.
                    uptimeView = mHolder.uptime;
                }
            }

            if (uptimeView != null) {
                mSetBackground = false;
                if (mFirstRunTime >= 0) {
                    //Log.i("foo", "Time for " + mItem.mDisplayLabel
                    //        + ": " + (SystemClock.uptimeMillis()-mFirstRunTime));
                    uptimeView.setText(DateUtils.formatElapsedTime(builder,
                            (SystemClock.elapsedRealtime() - mFirstRunTime) / 1000));
                } else {
                    boolean isService = false;
                    if (mItem instanceof RunningState.MergedItem) {
                        isService = ((RunningState.MergedItem) mItem).mServices.size() > 0;
                    }
                    if (isService) {
                        uptimeView.setText(context.getResources().getText(
                                R.string.service_restarting));
                    } else {
                        uptimeView.setText("");
                    }
                }
            }
        }
    }

    public static class ViewHolder {
        public View rootView;
        public ImageView icon;
        public TextView name;
        public TextView description;
        public TextView size;
        public TextView uptime;

        public ViewHolder(View v) {
            rootView = v;
            icon = v.findViewById(R.id.icon);
            name = v.findViewById(R.id.name);
            description = v.findViewById(R.id.description);
            size = v.findViewById(R.id.size);
            uptime = v.findViewById(R.id.uptime);
            v.setTag(this);
        }

        public ActiveItem bind(RunningState state, RunningState.BaseItem item,
                               StringBuilder builder) {
            PackageManager pm = rootView.getContext().getPackageManager();
            if (item.mPackageInfo == null && item instanceof RunningState.MergedItem) {
                // Items for background processes don't normally load
                // their labels for performance reasons.  Do it now.
                RunningState.MergedItem mergedItem = (RunningState.MergedItem) item;
                if (mergedItem.mProcess != null) {
                    ((RunningState.MergedItem) item).mProcess.ensureLabel(pm);
                    item.mPackageInfo = ((RunningState.MergedItem) item).mProcess.mPackageInfo;
                    item.mDisplayLabel = ((RunningState.MergedItem) item).mProcess.mDisplayLabel;
                }
            }
            name.setText(item.mDisplayLabel);
            ActiveItem ai = new ActiveItem();
            ai.mRootView = rootView;
            ai.mItem = item;
            ai.mHolder = this;
            ai.mFirstRunTime = item.mActiveSince;
            if (item.mBackground) {
                description.setText(rootView.getContext().getText(R.string.cached));
            } else {
                description.setText(item.mDescription);
            }
            if (item instanceof RunningState.MergedItem) {
                RunningState.MergedItem mergedItem = (RunningState.MergedItem) item;
                DataBindingAdapters.setMergeItemDesc(description, mergedItem);
            }
            if (item instanceof RunningState.ProcessItem) {
                RunningState.ProcessItem processItem = (RunningState.ProcessItem) item;
                description.setText(description.getResources().getString(R.string.service_process_name, processItem.mProcessName));
            }
            item.mCurSizeStr = null;
            // icon.setImageDrawable(item.loadIcon(rootView.getContext(), state));
            AppInfo commonPackageInfo = new AppInfo();
            commonPackageInfo.setPkgName(item.mPackageInfo.packageName);
            GlideApp.with(rootView.getContext())
                    .load(commonPackageInfo)
                    .placeholder(0)
                    .error(R.mipmap.ic_launcher_round)
                    .fallback(R.mipmap.ic_launcher_round)
                    .transition(withCrossFade())
                    .into(icon);
            icon.setVisibility(View.VISIBLE);
            ai.updateTime(rootView.getContext(), builder);
            return ai;
        }
    }

    @SuppressLint("AppCompatCustomView")
    static class TimeTicker extends TextView {
        public TimeTicker(Context context, AttributeSet attrs) {
            super(context, attrs);
        }
    }

    class ServiceListAdapter extends BaseAdapter {
        final RunningState mState;
        final LayoutInflater mInflater;
        boolean mShowBackground;
        ArrayList<RunningState.MergedItem> mOrigItems;
        final ArrayList<RunningState.MergedItem> mItems
                = new ArrayList<RunningState.MergedItem>();

        ServiceListAdapter(RunningState state) {
            mState = state;
            mInflater = (LayoutInflater) getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            refreshItems();
        }

        void refreshItems() {
            ArrayList<RunningState.MergedItem> newItems =
                    mShowBackground ? mState.getCurrentBackgroundItems()
                            : mState.getCurrentMergedItems();
            if (mOrigItems != newItems) {
                mOrigItems = newItems;
                if (newItems == null) {
                    mItems.clear();
                } else {
                    mItems.clear();
                    mItems.addAll(newItems);
                    if (mShowBackground) {
                        Collections.sort(mItems, mState.mBackgroundComparator);
                    }
                }
            }
        }

        public boolean hasStableIds() {
            return true;
        }

        public int getCount() {
            return mItems.size();
        }

        @Override
        public boolean isEmpty() {
            return mItems.size() == 0;
        }

        public Object getItem(int position) {
            return mItems.get(position);
        }

        public long getItemId(int position) {
            return mItems.get(position).hashCode();
        }

        public boolean areAllItemsEnabled() {
            return false;
        }

        public boolean isEnabled(int position) {
            return !mItems.get(position).mIsProcess;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View v;
            if (convertView == null) {
                v = newView(parent);
            } else {
                v = convertView;
            }
            bindView(v, position);
            return v;
        }

        public View newView(ViewGroup parent) {
            View v = mInflater.inflate(R.layout.running_processes_item, parent, false);
            new ViewHolder(v);
            return v;
        }

        public void bindView(View view, int position) {
            if (position >= mItems.size()) {
                // List must have changed since we last reported its
                // size...  ignore here, we will be doing a data changed
                // to refresh the entire list.
                return;
            }
            ViewHolder vh = (ViewHolder) view.getTag();
            RunningState.MergedItem item = mItems.get(position);
            ActiveItem ai = vh.bind(mState, item, mBuilder);
            mActiveItems.put(view, ai);
        }
    }

    void refreshUi(boolean dataChanged) {
        if (dataChanged) {
            ServiceListAdapter adapter = mAdapter;
            adapter.refreshItems();
            adapter.notifyDataSetChanged();
        }

        if (mDataAvail != null) {
            mDataAvail.run();
            mDataAvail = null;
        }

        mMemInfoReader.readMemInfo();

        /*
        // This is the amount of available memory until we start killing
        // background services.
        long availMem = mMemInfoReader.getFreeSize() + mMemInfoReader.getCachedSize()
                - SECONDARY_SERVER_MEM;
        if (availMem < 0) {
            availMem = 0;
        }
        */

        if (mCurShowCached != mAdapter.mShowBackground) {
            mCurShowCached = mAdapter.mShowBackground;
            if (mCurShowCached) {
                mForegroundProcessPrefix.setText(getResources().getText(
                        R.string.running_processes_header_used_prefix));
                mAppsProcessPrefix.setText(getResources().getText(
                        R.string.running_processes_header_cached_prefix));
            } else {
                mForegroundProcessPrefix.setText(getResources().getText(
                        R.string.running_processes_header_system_prefix));
                mAppsProcessPrefix.setText(getResources().getText(
                        R.string.running_processes_header_apps_prefix));
            }
        }

        final long totalRam = mMemInfoReader.getTotalSize();
        final long medRam;
        final long lowRam;
        if (mCurShowCached) {
            lowRam = mMemInfoReader.getFreeSize() + mMemInfoReader.getCachedSize();
            medRam = mState.mBackgroundProcessMemory;
        } else {
            lowRam = mMemInfoReader.getFreeSize() + mMemInfoReader.getCachedSize()
                    + mState.mBackgroundProcessMemory;
            medRam = mState.mServiceProcessMemory;

        }
        final long highRam = totalRam - medRam - lowRam;

        if (mCurTotalRam != totalRam || mCurHighRam != highRam || mCurMedRam != medRam
                || mCurLowRam != lowRam) {
            mCurTotalRam = totalRam;
            mCurHighRam = highRam;
            mCurMedRam = medRam;
            mCurLowRam = lowRam;
            BidiFormatter bidiFormatter = BidiFormatter.getInstance();
            String sizeStr = bidiFormatter.unicodeWrap(
                    Formatter.formatShortFileSize(getContext(), lowRam));
            mBackgroundProcessText.setText(getResources().getString(
                    R.string.running_processes_header_ram, sizeStr));
            sizeStr = bidiFormatter.unicodeWrap(
                    Formatter.formatShortFileSize(getContext(), medRam));
            mAppsProcessText.setText(getResources().getString(
                    R.string.running_processes_header_ram, sizeStr));
            sizeStr = bidiFormatter.unicodeWrap(
                    Formatter.formatShortFileSize(getContext(), highRam));
            mForegroundProcessText.setText(getResources().getString(
                    R.string.running_processes_header_ram, sizeStr));
            mColorBar.setRatios(highRam / (float) totalRam,
                    medRam / (float) totalRam,
                    lowRam / (float) totalRam);
        }
    }

    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        ListView l = (ListView) parent;
        RunningState.MergedItem mi = (RunningState.MergedItem) l.getAdapter().getItem(position);
        mCurSelected = mi;
        // startServiceDetailsActivity(mi);
    }

    public void onMovedToScrapHeap(View view) {
        mActiveItems.remove(view);
    }

    public RunningProcessesView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void doCreate() {
        mAm = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
        mState = RunningState.getInstance(getContext());
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.running_processes_view, this);
        mListView = findViewById(android.R.id.list);
        mListView.setOnItemClickListener(this);
        mListView.setRecyclerListener(this);
        mAdapter = new ServiceListAdapter(mState);
        mListView.setAdapter(mAdapter);
        mHeader = inflater.inflate(R.layout.running_processes_header, null);
        mListView.addHeaderView(mHeader, null, false /* set as not selectable */);
        mColorBar = mHeader.findViewById(R.id.color_bar);
        final Context context = getContext();
        mColorBar.setColors(context.getResources().getColor(R.color.running_processes_system_ram),
                Utils.getColorAccent(context), context.getResources().getColor(R.color.running_processes_free_ram));
        mBackgroundProcessPrefix = mHeader.findViewById(R.id.freeSizePrefix);
        mAppsProcessPrefix = mHeader.findViewById(R.id.appsSizePrefix);
        mForegroundProcessPrefix = mHeader.findViewById(R.id.systemSizePrefix);
        mBackgroundProcessText = mHeader.findViewById(R.id.freeSize);
        mAppsProcessText = mHeader.findViewById(R.id.appsSize);
        mForegroundProcessText = mHeader.findViewById(R.id.systemSize);

        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        mAm.getMemoryInfo(memInfo);
        //SECONDARY_SERVER_MEM = memInfo.secondaryServerThreshold;
    }

}
