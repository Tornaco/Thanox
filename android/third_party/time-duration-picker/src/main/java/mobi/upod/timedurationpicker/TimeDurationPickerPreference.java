package mobi.upod.timedurationpicker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

/**
 * A preference that allows the user to pick a time duration using a {@link TimeDurationPicker}.
 * <p>
 * Use this like every other preference (for example a {@code EditTextPreference}) in your preference XML file, but
 * be aware of the following:
 * <ol>
 * <li>The {@code android:defaultValue} specifies the default duration in milliseconds.
 * <li>You can use one of the {@code PLACEHOLDER_*} strings in your summary which will be replaced by the duration.
 * For example a summary could look like {@code "Remind me in ${m:ss} minute(s)."}
 * </ol>
 *
 * @see TimeDurationPicker
 * @see TimeDurationPickerDialog
 */
public class TimeDurationPickerPreference extends DialogPreference {
    /**
     * Placeholder in the summary that will be replaced by the current duration value.
     */
    public static final String PLACEHOLDER_HOURS_MINUTES_SECONDS = "${h:mm:ss}";
    /**
     * Placeholder in the summary that will be replaced by the current duration value.
     */
    public static final String PLACEHOLDER_MINUTES_SECONDS = "${m:ss}";
    /**
     * Placeholder in the summary that will be replaced by the current duration value.
     */
    public static final String PLACEHOLDER_SECONDS = "${s}";

    private long duration = 0;
    private TimeDurationPicker picker = null;
    private String summaryTemplate;

    public TimeDurationPickerPreference(Context context) {
        this(context, null);
    }

    public TimeDurationPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
    }

    /**
     * Set the current duration.
     *
     * @param duration duration in milliseconds
     */
    public void setDuration(long duration) {
        this.duration = duration;
        persistLong(duration);
        notifyDependencyChange(shouldDisableDependents());
        notifyChanged();
    }

    /**
     * Get the current duration.
     *
     * @return duration in milliseconds.
     */
    public long getDuration() {
        return duration;
    }

    /**
     * Gets the {@link TimeDurationPicker} used by this dialog.
     *
     * @return the picker used by this dialog.
     */
    public TimeDurationPicker getTimeDurationPicker() {
        return picker;
    }

    //
    // internal stuff
    //

    private void updateDescription() {
        if (summaryTemplate == null) {
            summaryTemplate = getSummary().toString();
        }
        final String summary = summaryTemplate
                .replace(PLACEHOLDER_HOURS_MINUTES_SECONDS, TimeDurationUtil.formatHoursMinutesSeconds(duration))
                .replace(PLACEHOLDER_MINUTES_SECONDS, TimeDurationUtil.formatMinutesSeconds(duration)
                        .replace(PLACEHOLDER_SECONDS, TimeDurationUtil.formatSeconds(duration)));
        setSummary(summary);
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder.setTitle(null).setIcon(null));
    }

    @Override
    protected View onCreateDialogView() {
        final LayoutInflater inflater = LayoutInflater.from(getContext());
        picker = initPicker((TimeDurationPicker) inflater.inflate(R.layout.time_duration_picker_dialog, null));
        return picker;
    }

    protected TimeDurationPicker initPicker(TimeDurationPicker timePicker) {
        return timePicker;
    }

    @Override
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);
        picker.setDuration(duration);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            final long newDuration = picker.getDuration();

            if (!callChangeListener(newDuration)) {
                return;
            }

            // persist
            setDuration(newDuration);
            updateDescription();
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return (long) a.getInt(index, 0);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        final long duration;
        if (restorePersistedValue)
            duration = getPersistedLong(0);
        else
            duration = Long.parseLong(defaultValue.toString());

        // need to persist here for default value to work
        setDuration(duration);
        updateDescription();
    }
}
