/*
 * Copyright (c) 2016 Nick Guo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package github.tornaco.android.thanos.main;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

import github.tornaco.android.thanos.R;
import com.elvishew.xlog.XLog;
import util.CollectionUtils;

@SuppressWarnings("WeakerAccess")
public class FragmentController<T extends Fragment> {

    private List<T> mPages;
    private FragmentManager mFragmentManager;

    private T mCurrent;

    private int mCurrentIndex = 0;

    private int mDefIndex = 0;

    private int containerId = R.id.container;

    public FragmentController(FragmentManager manager, List<T> safeFragments, int id) {
        this.mFragmentManager = manager;
        this.mPages = safeFragments;
        this.containerId = id;
        init();
    }

    private void init() {
        FragmentManager fragmentManager = mFragmentManager;
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        List<Fragment> old = fragmentManager.getFragments();

        if (!CollectionUtils.isNullOrEmpty(old)) {
            for (Fragment fragment : old) {
                transaction.remove(fragment);
                XLog.v("Removed %s", fragment);
            }
        }

        for (Fragment fragment : mPages) {
            transaction.add(containerId, fragment, fragment.getClass().getSimpleName());
            transaction.hide(fragment);
        }

        transaction.commitAllowingStateLoss();
    }

    public List<T> getPages() {
        return mPages;
    }

    public void setDefaultIndex(int index) {
        mDefIndex = index;
    }

    public T getCurrent() {
        return mCurrent == null ? mPages.get(mDefIndex) : mCurrent;
    }

    public int getCurrentIndex() {
        return mCurrentIndex;
    }

    public void setCurrent(int index) {
        FragmentManager fragmentManager = mFragmentManager;
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.grow_fade_in, 0);
        transaction.hide(getCurrent());
        T current = mPages.get(index);
        transaction.show(current);
        transaction.commitAllowingStateLoss();
        mCurrent = current;
        mCurrentIndex = index;
    }
}
