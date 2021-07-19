package util;

public class PinyinComparatorUtils {
    private static final PinyinComparator COMPARATOR = new PinyinComparator();

    public static int compare(String o1, String o2) {
        return COMPARATOR.compare(o1, o2);
    }

    public static int compare(CharSequence o1, CharSequence o2) {
        return COMPARATOR.compare(String.valueOf(o1), String.valueOf(o2));
    }
}
