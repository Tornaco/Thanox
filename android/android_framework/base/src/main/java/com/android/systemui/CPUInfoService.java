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

package com.android.systemui;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.FileReader;

public class CPUInfoService extends Service {
    private View mView;
    private Thread mCurCPUThread;
    private final String TAG = "CPUInfoService";
    private int mNumCpus = 2;
    private String[] mCpu = null;
    private String[] mCurrFreq = null;
    private String[] mCurrGov = null;

    private int CPU_TEMP_DIVIDER = 1;
    private String CPU_TEMP_SENSOR = "/sys/class/thermal/thermal_zone0/temp";
    private String DISPLAY_CPUS = "";
    private boolean mCpuTempAvail;

    private static final String NUM_OF_CPUS_PATH = "/sys/devices/system/cpu/present";
    private static final String CURRENT_CPU = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq";
    private static final String CPU_ROOT = "/sys/devices/system/cpu/cpu";
    private static final String CPU_CUR_TAIL = "/cpufreq/scaling_cur_freq";
    private static final String CPU_GOV_TAIL = "/cpufreq/scaling_governor";

    private class CPUView extends View {
        private Paint mOnlinePaint;
        private Paint mOfflinePaint;
        private float mAscent;
        private int mFH;
        private int mMaxWidth;

        private int mNeededWidth;
        private int mNeededHeight;
        private String mCpuTemp;

        private boolean mDataAvail;

        private Handler mCurCPUHandler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.obj == null) {
                    return;
                }
                if (msg.what == 1) {
                    String msgData = (String) msg.obj;
                    try {
                        String[] parts = msgData.split(";");
                        mCpuTemp = parts[0];

                        String[] cpuParts = parts[1].split("\\|");
                        for (int i = 0; i < cpuParts.length; i++) {
                            String cpuInfo = cpuParts[i];
                            String cpuInfoParts[] = cpuInfo.split(":");
                            if (cpuInfoParts.length == 3) {
                                mCurrFreq[i] = cpuInfoParts[1];
                                mCurrGov[i] = cpuInfoParts[2];
                            } else {
                                mCurrFreq[i] = "0";
                                mCurrGov[i] = "";
                            }
                        }
                        mDataAvail = true;
                        updateDisplay();
                    } catch (ArrayIndexOutOfBoundsException e) {
                        Log.e(TAG, "illegal data " + msgData);
                    }
                }
            }
        };

        CPUView(Context c) {
            super(c);
            float density = c.getResources().getDisplayMetrics().density;
            int paddingPx = Math.round(5 * density);
            setPadding(paddingPx, paddingPx, paddingPx, paddingPx);
            setBackgroundColor(Color.argb(0x60, 0, 0, 0));

            final int textSize = Math.round(12 * density);

            Typeface typeface = Typeface.create("monospace", Typeface.NORMAL);

            mOnlinePaint = new Paint();
            mOnlinePaint.setTypeface(typeface);
            mOnlinePaint.setAntiAlias(true);
            mOnlinePaint.setTextSize(textSize);
            mOnlinePaint.setColor(Color.WHITE);
            mOnlinePaint.setShadowLayer(5.0f, 0.0f, 0.0f, Color.BLACK);

            mOfflinePaint = new Paint();
            mOfflinePaint.setTypeface(typeface);
            mOfflinePaint.setAntiAlias(true);
            mOfflinePaint.setTextSize(textSize);
            mOfflinePaint.setColor(Color.RED);

            mAscent = mOnlinePaint.ascent();
            float descent = mOnlinePaint.descent();
            mFH = (int) (descent - mAscent + .5f);

            final String maxWidthStr = "cpuX: interactive 00000000";
            mMaxWidth = (int) mOnlinePaint.measureText(maxWidthStr);

            updateDisplay();
        }

        @Override
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
        }

        @Override
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            mCurCPUHandler.removeMessages(1);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            setMeasuredDimension(resolveSize(mNeededWidth, widthMeasureSpec),
                    resolveSize(mNeededHeight, heightMeasureSpec));
        }

        private String getCPUInfoString(int i) {
            String cpu = mCpu[i];
            String freq = mCurrFreq[i];
            String gov = mCurrGov[i];
            return "cpu" + cpu + ": " + gov + " " + String.format("%8s", toMHz(freq));
        }

        private String getCpuTemp(String cpuTemp) {
            if (CPU_TEMP_DIVIDER > 1) {
                return String.format("%s",
                        Integer.parseInt(cpuTemp) / CPU_TEMP_DIVIDER);
            } else {
                return cpuTemp;
            }
        }

        @Override
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (!mDataAvail) {
                return;
            }

            final int W = mNeededWidth;
            final int RIGHT = getWidth() - 1;

            int x = RIGHT - mPaddingRight;
            int top = mPaddingTop + 2;
            int bottom = mPaddingTop + mFH - 2;

            int y = mPaddingTop - (int) mAscent;

            if (!mCpuTemp.equals("0")) {
                canvas.drawText("Temp: " + getCpuTemp(mCpuTemp) + "°C",
                        RIGHT - mPaddingRight - mMaxWidth, y - 1, mOnlinePaint);
                y += mFH;
            }

            for (int i = 0; i < mCurrFreq.length; i++) {
                String s = getCPUInfoString(i);
                String freq = mCurrFreq[i];
                if (!freq.equals("0")) {
                    canvas.drawText(s, RIGHT - mPaddingRight - mMaxWidth,
                            y - 1, mOnlinePaint);
                } else {
                    canvas.drawText("cpu" + mCpu[i] + ": offline", RIGHT - mPaddingRight - mMaxWidth,
                            y - 1, mOfflinePaint);
                }
                y += mFH;
            }
        }

        void updateDisplay() {
            if (!mDataAvail) {
                return;
            }
            final int NW = mNumCpus;

            int neededWidth = mPaddingLeft + mPaddingRight + mMaxWidth;
            int neededHeight = mPaddingTop + mPaddingBottom + (mFH * ((mCpuTempAvail ? 1 : 0) + NW));
            if (neededWidth != mNeededWidth || neededHeight != mNeededHeight) {
                mNeededWidth = neededWidth;
                mNeededHeight = neededHeight;
                requestLayout();
            } else {
                invalidate();
            }
        }

        private String toMHz(String mhzString) {
            return new StringBuilder().append(Integer.valueOf(mhzString) / 1000).append(" MHz").toString();
        }

        public Handler getHandler() {
            return mCurCPUHandler;
        }
    }

    protected class CurCPUThread extends Thread {
        private boolean mInterrupt = false;
        private Handler mHandler;

        public CurCPUThread(Handler handler, int numCpus) {
            mHandler = handler;
            mNumCpus = numCpus;
        }

        public void interrupt() {
            mInterrupt = true;
        }

        @Override
        public void run() {
            try {
                while (!mInterrupt) {
                    sleep(500);
                    StringBuffer sb = new StringBuffer();
                    String cpuTemp = CPUInfoService.readOneLine(CPU_TEMP_SENSOR);
                    sb.append(cpuTemp == null ? "0" : cpuTemp);
                    sb.append(";");

                    for (int i = 0; i < mNumCpus; i++) {
                        final String currCpu = mCpu[i];
                        final String freqFile = CPU_ROOT + mCpu[i] + CPU_CUR_TAIL;
                        String currFreq = CPUInfoService.readOneLine(freqFile);
                        final String govFile = CPU_ROOT + mCpu[i] + CPU_GOV_TAIL;
                        String currGov = CPUInfoService.readOneLine(govFile);

                        if (currFreq == null) {
                            currFreq = "0";
                            currGov = "";
                        }

                        sb.append(currCpu + ":" + currFreq + ":" + currGov + "|");
                    }
                    sb.deleteCharAt(sb.length() - 1);
                    mHandler.sendMessage(mHandler.obtainMessage(1, sb.toString()));
                }
            } catch (InterruptedException e) {
                return;
            }
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();

        mNumCpus = getCpus(DISPLAY_CPUS);
        mCurrFreq = new String[mNumCpus];
        mCurrGov = new String[mNumCpus];

        mCpuTempAvail = readOneLine(CPU_TEMP_SENSOR) != null;

        mView = new CPUView(this);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SECURE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.RIGHT | Gravity.TOP;
        params.setTitle("CPU Info");

        startThread();

        IntentFilter screenStateFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        screenStateFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mScreenStateReceiver, screenStateFilter);

        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.addView(mView, params);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopThread();
        ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(mView);
        mView = null;
        unregisterReceiver(mScreenStateReceiver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private static String readOneLine(String fname) {
        BufferedReader br;
        String line = null;
        try {
            br = new BufferedReader(new FileReader(fname), 512);
            try {
                line = br.readLine();
            } finally {
                br.close();
            }
        } catch (Exception e) {
            return null;
        }
        return line;
    }

    private int getCpus(String displayCpus) {
        int numOfCpu = 1;
        String[] cpuList = null;

        if (displayCpus != null) {
            cpuList = displayCpus.split(",");
            if (cpuList.length > 0) {
                numOfCpu = cpuList.length;
                mCpu = new String[numOfCpu];

                for (int i = 0; i < numOfCpu; i++) {
                    try {
                        int cpu = Integer.parseInt(cpuList[i]);
                        mCpu[i] = cpuList[i];
                    } catch (NumberFormatException ex) {
                        // derped overlay
                        return getCpus(null);
                    }
                }
            } else {
                // derped overlay
                return getCpus(null);
            }
        } else {
            // empty overlay, take all cores
            String numOfCpus = readOneLine(NUM_OF_CPUS_PATH);
            cpuList = numOfCpus.split("-");
            if (cpuList.length > 1) {
                try {
                    int cpuStart = Integer.parseInt(cpuList[0]);
                    int cpuEnd = Integer.parseInt(cpuList[1]);

                    numOfCpu = cpuEnd - cpuStart + 1;

                    if (numOfCpu < 0)
                        numOfCpu = 1;
                } catch (NumberFormatException ex) {
                    numOfCpu = 1;
                }
            }

            mCpu = new String[numOfCpu];
            for (int i = 0; i < numOfCpu; i++) {
                mCpu[i] = String.valueOf(i);
            }
        }
        return numOfCpu;
    }

    private BroadcastReceiver mScreenStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                Log.d(TAG, "ACTION_SCREEN_ON ");
                startThread();
                mView.setVisibility(View.VISIBLE);
            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                Log.d(TAG, "ACTION_SCREEN_OFF");
                mView.setVisibility(View.GONE);
                stopThread();
            }
        }
    };

    private void startThread() {
        Log.d(TAG, "started CurCPUThread");
        mCurCPUThread = new CurCPUThread(mView.getHandler(), mNumCpus);
        mCurCPUThread.start();
    }

    private void stopThread() {
        if (mCurCPUThread != null && mCurCPUThread.isAlive()) {
            Log.d(TAG, "stopping CurCPUThread");
            mCurCPUThread.interrupt();
            try {
                mCurCPUThread.join();
            } catch (InterruptedException e) {
            }
        }
        mCurCPUThread = null;
    }
}