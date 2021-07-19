/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.nononsenseapps.filepicker;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import static com.nononsenseapps.filepicker.Utils.isValidFileName;

public class NewFolderFragment extends NewItemFragment {

    private static final String TAG = "new_folder_fragment";

    public static void showDialog(@NonNull final FragmentManager fm,
                                  @Nullable final OnNewFolderListener listener) {
        NewItemFragment d = new NewFolderFragment();
        d.setListener(listener);
        d.show(fm, TAG);
    }

    @Override
    protected boolean validateName(@Nullable final String itemName) {
        return isValidFileName(itemName);
    }
}
