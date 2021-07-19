package github.tornaco.android.thanos.core.util;

public enum YesNoDontKnow {
    YES(1), NO(0), DONT_KNOW(-1);

    public int code;

    YesNoDontKnow(int code) {
        this.code = code;
    }
}
