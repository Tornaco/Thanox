package mobi.upod.timedurationpicker;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

/**
 * Base class for implementing a time duration picker dialog fragment as described in the
 * <a href="https://developer.android.com/guide/topics/ui/controls/pickers.html">Pickers</a> guide in the
 * android documentation.
 * <p>
 * You need to implement #onDurationSet in your derived class. You can override #getInitialDuration if you want to
 * provide an initial duration to be set when the dialog starts.
 *
 * @see TimeDurationPickerDialog
 * @see TimeDurationPicker
 */
public abstract class TimeDurationPickerDialogFragment
        extends DialogFragment implements TimeDurationPickerDialog.OnDurationSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new TimeDurationPickerDialog(getActivity(), this, getInitialDuration(), setTimeUnits());
    }

    /**
     * The duration to be shown as default value when the dialog appears.
     *
     * @return the default duration in milliseconds.
     */
    protected long getInitialDuration() {
        return 0;
    }

    protected int setTimeUnits() {
        return TimeDurationPicker.HH_MM_SS;
    }
}
