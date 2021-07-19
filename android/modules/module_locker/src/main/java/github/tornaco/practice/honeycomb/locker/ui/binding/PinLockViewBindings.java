/*
 * Copyright 2016, The Android Open Source Project
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

package github.tornaco.practice.honeycomb.locker.ui.binding;


import android.view.animation.AnimationUtils;

import androidx.databinding.BindingAdapter;
import androidx.databinding.Observable;

import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;

import github.tornaco.practice.honeycomb.locker.R;
import github.tornaco.practice.honeycomb.locker.ui.setup.SetupViewModel;
import github.tornaco.practice.honeycomb.locker.ui.verify.VerifyViewModel;

public class PinLockViewBindings {

    @BindingAdapter("android:verifyAction")
    public static void bindVerifyPattern(PinLockView view, VerifyViewModel verifyViewModel) {
        view.setPinLockListener(new PinLockListener() {
            @Override
            public void onComplete(String pin) {
                verifyViewModel.verify(pin);
            }

            @Override
            public void onEmpty() {

            }

            @Override
            public void onPinChange(int pinLength, String intermediatePin) {

            }
        });

        verifyViewModel.failCount.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                view.resetPinLockView();
                view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.shake));
            }
        });
        verifyViewModel.verified.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                view.resetPinLockView();
            }
        });
    }

    @BindingAdapter("android:verifyAction")
    public static void bindVerifyPattern(PinLockView view, SetupViewModel setupViewModel) {
        setupViewModel.onStartInput();
        view.setPinLockListener(new PinLockListener() {
            @Override
            public void onComplete(String pin) {
                setupViewModel.onInputComplete(pin);
            }

            @Override
            public void onEmpty() {

            }

            @Override
            public void onPinChange(int pinLength, String intermediatePin) {

            }
        });
        setupViewModel.stage.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                view.resetPinLockView();
            }
        });
    }

}
