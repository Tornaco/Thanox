package mobi.upod.timedurationpicker;

/**
 * Utility class for handling duration values.
 */
public class TimeDurationUtil {
    /**
     * The number of milliseconds within a second.
     */
    public static final int MILLIS_PER_SECOND = 1000;
    /**
     * The number of milliseconds within a minute.
     */
    public static final int MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND;
    /**
     * The number of milliseconds within an hour.
     */
    public static final int MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE;

    /**
     * Calculates the number of hours within the specified duration.
     *
     * @param duration duration in milliseconds
     * @return number of hours within the specified duration.
     */
    public static int hoursOf(long duration) {
        return (int) duration / MILLIS_PER_HOUR;
    }

    /**
     * Calculates the full number of minutes within the specified duration.
     *
     * @param duration duration in milliseconds
     * @return number of minutes within the specified duration.
     */
    public static int minutesOf(long duration) {
        return (int) duration / MILLIS_PER_MINUTE;
    }

    /**
     * Calculates the number of minutes within the specified duration excluding full hours.
     *
     * @param duration duration in milliseconds
     * @return number of minutes within the specified duration.
     */
    public static int minutesInHourOf(long duration) {
        return (int) (duration - hoursOf(duration) * MILLIS_PER_HOUR) / MILLIS_PER_MINUTE;
    }

    /**
     * Calculates the full number of seconds within the specified duration.
     *
     * @param duration duration in milliseconds
     * @return number of seconds within the specified duration.
     */
    public static int secondsOf(long duration) {
        return (int) duration / MILLIS_PER_SECOND;
    }

    /**
     * Calculates the number of seconds within the specified duration excluding full minutes.
     *
     * @param duration duration in milliseconds
     * @return number of seconds within the specified duration.
     */
    public static int secondsInMinuteOf(long duration) {
        return (int) (duration - hoursOf(duration) * MILLIS_PER_HOUR - minutesInHourOf(duration) * MILLIS_PER_MINUTE) / MILLIS_PER_SECOND;
    }

    /**
     * Calculates a duration from hours, minutes and seconds.
     *
     * @param hours   full hours of the duration
     * @param minutes full minutes of the duration
     * @param seconds full seconds of the duration
     * @return duration in milliseconds.
     */
    public static long durationOf(int hours, int minutes, int seconds) {
        return hours * MILLIS_PER_HOUR + minutes * MILLIS_PER_MINUTE + seconds * MILLIS_PER_SECOND;
    }

    /**
     * Returns a string representing the specified duration in the format {@code h:mm:ss}.
     *
     * @param duration duration in milliseconds
     * @return string representation of the duration.
     */
    public static String formatHoursMinutesSeconds(long duration) {
        return String.format("%d:%02d:%02d", hoursOf(duration), minutesInHourOf(duration), secondsInMinuteOf(duration));
    }

    /**
     * Returns a string representing the specified duration in the format {@code m:ss}.
     *
     * @param duration duration in milliseconds
     * @return string representation of the duration.
     */
    public static String formatMinutesSeconds(long duration) {
        return String.format("%d:%02d", minutesOf(duration), secondsInMinuteOf(duration));
    }

    /**
     * Returns a string representing the specified duration in the format {@code s}.
     *
     * @param duration duration in milliseconds
     * @return string representation of the duration.
     */
    public static String formatSeconds(long duration) {
        return String.format("%d", secondsInMinuteOf(duration));
    }
}
