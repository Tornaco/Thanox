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
