package github.tornaco.android.thanos.core.app.activity;

public final class VerifyResult {
    public static final int ALLOW = 1;
    public static final int IGNORE = -1;

    public static final int REASON_USER_INPUT_CORRECT = 0;
    public static final int REASON_USER_INPUT_INCORRECT = 1;
    public static final int REASON_USER_FP_INCORRECT = 2;
    public static final int REASON_USER_KEY_NOT_SET = 3;
    public static final int REASON_USER_CANCEL = -1;
}

