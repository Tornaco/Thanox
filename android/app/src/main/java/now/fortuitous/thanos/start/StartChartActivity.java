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

package now.fortuitous.thanos.start;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.core.app.ActivityManager;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.util.PkgUtils;
import github.tornaco.android.thanos.databinding.ActivityStartChartBinding;
import github.tornaco.android.thanos.theme.ThemeActivity;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.android.thanos.util.TypefaceHelper;
import now.fortuitous.thanos.apps.AppDetailsActivity;

public class StartChartActivity extends ThemeActivity implements OnChartValueSelectedListener {

    private ActivityStartChartBinding binding;

    private Category category = Category.Blocked;


    public static void start(Context context) {
        ActivityUtils.startActivity(context, StartChartActivity.class);
    }

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStartChartBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        setupView();
        setData();
    }

    private void setupView() {
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        binding.showDetailedRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailedStartRecordsActivity.start(thisActivity(), null);
            }
        });

        //Creating the ArrayAdapter instance having the category list
        String[] category = getResources().getStringArray(R.array.start_record_chart_categories);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, github.tornaco.android.thanos.module.common.R.layout.ghost_text_view, category);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        binding.spinner.setAdapter(arrayAdapter);
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l) {
                setTitle(category[index]);
                binding.toolbar.setTitle(category[index]);
                StartChartActivity.this.category = Category.values()[index];
                setData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        int[] attrs = {
                android.R.attr.textColorPrimary,
                android.R.attr.windowBackground,
                android.R.attr.windowBackground};
        TypedArray ta = obtainStyledAttributes(attrs);
        @SuppressLint("ResourceType") int textColorPrimaryRes = ta.getResourceId(0, github.tornaco.android.thanos.module.common.R.color.md_red_700);
        @SuppressLint("ResourceType") int windowBgColorRes = ta.getResourceId(1, github.tornaco.android.thanos.module.common.R.color.md_white);
        @SuppressLint("ResourceType") int cardBgColorRes = ta.getResourceId(2, github.tornaco.android.thanos.module.common.R.color.md_white);
        ta.recycle();
        int textColorPrimary = getColor(textColorPrimaryRes);
        int windowBgColor = getColor(windowBgColorRes);
        int cardBgColor = getColor(cardBgColorRes);

        PieChart chart = binding.chart1;

        chart.getDescription().setEnabled(true);
        chart.getDescription().setText("Powered by thanox");
        chart.getDescription().setTextColor(textColorPrimary);
        chart.getDescription().setTypeface(TypefaceHelper.googleSans(this));
        chart.setExtraOffsets(5, 10, 5, 5);

        chart.setDragDecelerationFrictionCoef(0.95f);

        chart.setCenterText(generateCenterSpannableText());
        chart.setCenterTextColor(textColorPrimary);
        chart.setCenterTextTypeface(TypefaceHelper.googleSansBold(this));
        chart.setDrawCenterText(true);

        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(cardBgColor);

        chart.setTransparentCircleColor(cardBgColor);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);

        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);

        // add a selection listener
        chart.setOnChartValueSelectedListener(this);

        chart.animateY(1400, Easing.EaseInOutQuad);
        // chart.spin(2000, 0, 360);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
        l.setTypeface(TypefaceHelper.googleSans(this));

        l.setTextColor(textColorPrimary);

        // entry label styling
        chart.setDrawEntryLabels(false);
        chart.setUsePercentValues(false);
        chart.setEntryLabelTypeface(TypefaceHelper.googleSansBold(this));
    }


    private SpannableString generateCenterSpannableText() {
        ThanosManager thanosManager = ThanosManager.from(getApplicationContext());
        long count = 0L;
        if (category == Category.Blocked) {
            count = thanosManager.getActivityManager().getStartRecordsBlockedCount();
        }
        if (category == Category.Allowed) {
            count = thanosManager.getActivityManager().getStartRecordsAllowedCount();
        }
        if (category == Category.Merged) {
            count = thanosManager.getActivityManager().getStartRecordsAllowedCount()
                    + thanosManager.getActivityManager().getStartRecordsBlockedCount();
        }
        return new SpannableString(String.format("%s times", count));
    }


    private void setData() {
        ThanosManager thanosManager = ThanosManager.from(getApplicationContext());
        if (!thanosManager.isServiceInstalled()) {
            return;
        }

        ActivityManager am = thanosManager.getActivityManager();

        List<String> startPkgs = new ArrayList<>();

        if (category == Category.Blocked || category == Category.Merged) {
            startPkgs.addAll(am.getStartRecordBlockedPackages());
        }
        if (category == Category.Allowed || category == Category.Merged) {
            startPkgs = am.getStartRecordAllowedPackages();
        }

        List<StartEntry> startEntries = new ArrayList<>();
        for (String pkg : startPkgs) {
            long count = 0L;
            if (category == Category.Blocked) {
                count = am.getStartRecordBlockedCountByPackageName(pkg);
            }
            if (category == Category.Allowed) {
                count = am.getStartRecordAllowedCountByPackageName(pkg);
            }
            if (category == Category.Merged) {
                count = am.getStartRecordBlockedCountByPackageName(pkg)
                        + am.getStartRecordAllowedCountByPackageName(pkg);
            }
            StartEntry e = new StartEntry(count, pkg);
            startEntries.add(e);
        }
        Collections.sort(startEntries);

        ArrayList<PieEntry> entries = new ArrayList<>();

        for (int i = 0; (i < startEntries.size() && i < 36); i++) {
            entries.add(new PieEntry(startEntries.get(i).times,
                    PkgUtils.loadNameByPkgName(this, startEntries.get(i).pkg) + " " + startEntries.get(i).times,
                    startEntries.get(i)));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.MATERIAL_COLORS)
            colors.add(c);


        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieChart chart = binding.chart1;

        chart.setCenterText(generateCenterSpannableText());

        PieData data = new PieData(dataSet);

        data.setValueFormatter(new PercentFormatter(chart) {
            @Override
            public String getPieLabel(float value, PieEntry pieEntry) {
                return "";
            }
        });
        data.setValueTypeface(TypefaceHelper.googleSans(this));
        data.setValueTextSize(9f);
        data.setValueTextColor(Color.WHITE);
        chart.setData(data);

        // undo all highlights
        chart.highlightValues(null);

        chart.invalidate();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        StartEntry entry = (StartEntry) e.getData();
        AppDetailsActivity.start(this, ThanosManager.from(this).getPkgManager().getAppInfo(entry.pkg));
    }

    @Override
    public void onNothingSelected() {

    }

    @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.start_restrict_chart_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.action_reset == item.getItemId()) {
            if (category == Category.Blocked) {
                ThanosManager.from(getApplicationContext())
                        .getActivityManager()
                        .resetStartRecordsBlocked();
            }
            if (category == Category.Allowed) {
                ThanosManager.from(getApplicationContext())
                        .getActivityManager()
                        .resetStartRecordsAllowed();
            }
            if (category == Category.Merged) {
                ThanosManager.from(getApplicationContext())
                        .getActivityManager()
                        .resetStartRecordsBlocked();
                ThanosManager.from(getApplicationContext())
                        .getActivityManager()
                        .resetStartRecordsAllowed();
            }

            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class StartEntry implements Comparable<StartEntry> {
        long times;
        String pkg;

        public StartEntry(long times, String pkg) {
            this.times = times;
            this.pkg = pkg;
        }

        @Override
        public int compareTo(@NonNull StartEntry o) {
            return -Long.compare(this.times, o.times);
        }
    }

    public enum Category {
        Blocked, Allowed, Merged
    }
}
