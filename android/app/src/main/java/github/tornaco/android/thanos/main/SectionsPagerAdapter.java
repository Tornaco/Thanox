package github.tornaco.android.thanos.main;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.common.collect.ImmutableList;

import java.util.List;

import github.tornaco.android.thanos.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
@Deprecated
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{
            R.string.nav_title_thanox,
            R.string.nav_title_plugin};

    private final Context context;

    private final List<NavFragment> pages =
            ImmutableList.of(
                    new PrebuiltFeatureFragment(),
                    new PluginFragment());

    public SectionsPagerAdapter(AppCompatActivity context, FragmentManager fm) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.context = context;
    }

    @Override
    @NonNull
    public Fragment getItem(int position) {
        return pages.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return context.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return pages.size();
    }
}