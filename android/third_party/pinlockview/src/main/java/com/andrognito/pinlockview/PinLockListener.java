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

package com.andrognito.pinlockview;

/**
 * The listener that triggers callbacks for various events
 * in the {@link PinLockView}
 *
 * Created by aritraroy on 31/05/16.
 */
public interface PinLockListener {

    /**
     * Triggers when the complete pin is entered,
     * depends on the pin length set by the user
     *
     * @param pin the complete pin
     */
    void onComplete(String pin);


    /**
     * Triggers when the pin is empty after manual deletion
     */
    void onEmpty();

    /**
     * Triggers on a key press on the {@link PinLockView}
     *
     * @param pinLength       the current pin length
     * @param intermediatePin the intermediate pin
     */
    void onPinChange(int pinLength, String intermediatePin);
}
