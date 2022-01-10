package github.tornaco.android.thanos.core.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Nick@NewStand.org on 2017/3/26 17:00
 * E-Mail: NewStand@163.com
 * All right reserved.
 */

public class DateUtils {

    private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd-HH:mm:ss";
    private static final String DATE_FORMAT_PATTERN_MESSAGE_TIME_SHORT = "HH:mm:ss";
    private static final String DATE_FORMAT_PATTERN_MESSAGE_TIME_LONG = "yyyy-MM-dd HH:mm:ss";

    public static String formatLong(long l) {
        String time;
        DateFormat format = DateFormat.getDateInstance(DateFormat.FULL);
        Date d1 = new Date(l);
        time = format.format(d1);
        DateFormat timeInstance = SimpleDateFormat.getTimeInstance(DateFormat.SHORT);
        return time + "\t" + timeInstance.format(d1);
    }

    public static String formatForFileName(long l) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.ENGLISH);
        Date d1 = new Date(l);
        return format.format(d1);
    }

    public static String formatShortForMessageTime(long l) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_PATTERN_MESSAGE_TIME_SHORT, Locale.ENGLISH);
        Date d1 = new Date(l);
        return format.format(d1);
    }

    public static String formatLongForMessageTime(long l) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_PATTERN_MESSAGE_TIME_LONG, Locale.ENGLISH);
        Date d1 = new Date(l);
        return format.format(d1);
    }

    public static long getToadyStartTimeInMills() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static String formatDuration(Duration duration) {
        long seconds = duration.getSeconds();
        long absSeconds = Math.abs(seconds);
        String positive = String.format(Locale.ENGLISH,
                "%d:%02d:%02d", absSeconds / 3600, (absSeconds % 3600) / 60, absSeconds % 60);
        return seconds < 0 ? "-" + positive : positive;
    }

    public static boolean isToday(Date date) {
        return isSameDay(new Date(), date);
    }

    public static boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
    }

    public static boolean isYesterday(Date date) {
        Calendar c1 = Calendar.getInstance(); // today
        c1.add(Calendar.DAY_OF_YEAR, -1); // yesterday

        Calendar c2 = Calendar.getInstance();
        c2.setTime(date); // your date

        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR);
    }

    public static boolean isTheDayBeforeYesterday(Date date) {
        Calendar c1 = Calendar.getInstance(); // today
        c1.add(Calendar.DAY_OF_YEAR, -2); // the day before yesterday

        Calendar c2 = Calendar.getInstance();
        c2.setTime(date); // your date

        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR);
    }
}