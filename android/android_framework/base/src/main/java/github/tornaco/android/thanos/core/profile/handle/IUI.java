package github.tornaco.android.thanos.core.profile.handle;

import android.content.pm.ActivityInfo;

import github.tornaco.android.thanos.core.annotation.NonNull;
import github.tornaco.android.thanos.core.annotation.Nullable;

@HandlerName("ui")
public
interface IUI {

    void showDanmu(@NonNull Object msg);

    /**
     * Show a danmu on your screen.
     *
     * @param icon Icon for danmu, supported format:
     *             app icon: app://com.app.package.name
     * @param msg  The text to show
     */
    void showDanmu(@Nullable String icon, @NonNull Object msg);

    /*
     * @hide For internal usage.
     */
    void showDanmu(@Nullable String icon, @NonNull Object msg, boolean clearCachedView);

    void showShortToast(@NonNull Object msg);

    void showLongToast(@NonNull Object msg);

    void showDialog(@Nullable String title,
                    @NonNull String msg,
                    @Nullable String yes);

    void showDialog(@Nullable String title,
                    @NonNull String msg,
                    @Nullable String yes,
                    @Nullable String no);

    void showNotification(
            @NonNull String notificationTag,
            @NonNull String title,
            @NonNull String msg,
            boolean important);

    void showNotification(
            @NonNull String notificationTag,
            @NonNull String title,
            @NonNull String msg,
            @Nullable String notificationAppName,
            @NonNull String pkgToLaunchOnClick,
            @NonNull String largeIcon,
            @NonNull String smallIcon,
            boolean vibrate,
            boolean sound,
            boolean important);

    void cancelNotification(@NonNull String notificationTag);

    void findAndClickViewByText(@NonNull String text);

    void findAndClickViewByText(@NonNull String text, @Nullable String componentNameShortString);

    void findAndClickViewById(@NonNull String id);

    void findAndClickViewById(@NonNull String id, @Nullable String componentNameShortString);

    void clickDelay(int x, int y, long delayMills);

    /**
     * @see ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
     * @see ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
     * @see ActivityInfo.SCREEN_ORIENTATION_USER
     */
    void requestScreenOrientation(int orientation);
}
