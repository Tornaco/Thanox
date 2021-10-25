package github.tornaco.android.thanos.main;

import android.content.Context;

import github.tornaco.android.thanos.app.Init;
import github.tornaco.android.thanos.app.PLayLvlCheckActivity;

public class NavActivityPlugin {
    public static boolean onCreate(Context context) {
        if (!Init.isLVLChecked(context) && Init.s == 0) {
            PLayLvlCheckActivity.Starter.INSTANCE.start(context);
            return true;
        }

        return false;
    }
}