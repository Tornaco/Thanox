/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.nononsenseapps.filepicker;


import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public abstract class NewItemFragment extends DialogFragment {

    private OnNewFolderListener listener = null;

    public NewItemFragment() {
        super();
    }

    public void setListener(@Nullable final OnNewFolderListener listener) {
        this.listener = listener;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
        builder.setView(R.layout.nnf_dialog_folder_name)
                .setTitle(R.string.nnf_new_folder)
                .setNegativeButton(R.string.nnf_new_folder_cancel,
                        null)
                .setPositiveButton(R.string.nnf_new_folder_ok,
                        null);

        final AlertDialog dialog = builder.create();

        dialog.setOnShowListener(dialog1 -> {
            final AlertDialog dialog2 = (AlertDialog) dialog1;
            final EditText editText = (EditText) dialog2.findViewById(R.id.edit_text);

            if (editText == null) {
                throw new NullPointerException("Could not find an edit text in the dialog");
            }

            Button cancel = dialog2.getButton(AlertDialog.BUTTON_NEGATIVE);
            cancel.setOnClickListener(view -> dialog2.cancel());

            final Button ok = dialog2.getButton(AlertDialog.BUTTON_POSITIVE);
            // Start disabled
            ok.setEnabled(false);
            ok.setOnClickListener(view -> {
                String itemName = editText.getText().toString();
                if (validateName(itemName)) {
                    if (listener != null) {
                        listener.onNewFolder(itemName);
                    }
                    dialog2.dismiss();
                }
            });

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(final CharSequence s, final int start,
                                              final int count, final int after) {
                }

                @Override
                public void onTextChanged(final CharSequence s, final int start,
                                          final int before, final int count) {
                }

                @Override
                public void afterTextChanged(final Editable s) {
                    ok.setEnabled(validateName(s.toString()));
                }
            });
        });


        return dialog;
    }

    protected abstract boolean validateName(final String itemName);

    public interface OnNewFolderListener {
        void onNewFolder(@NonNull final String name);
    }
}
