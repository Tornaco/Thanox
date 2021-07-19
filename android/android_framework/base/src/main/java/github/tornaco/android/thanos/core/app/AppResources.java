package github.tornaco.android.thanos.core.app;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.util.Log;
import github.tornaco.android.thanos.core.util.PkgUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.CONTEXT_IGNORE_SECURITY;

/**
 * Created by Tornaco on 2018/4/4 10:56.
 * God bless no bug!
 */

@AllArgsConstructor
@Getter
public class AppResources {

    private static final String LOG_TAG = "AppResources";
    private static final boolean DEBUG = false;

    private static final Map<Object, String> S_STRING_RES_CACHE = new HashMap<>();
    private static final Map<Object, String[]> S_STRING_ARRAY_RES_CACHE = new HashMap<>();

    private Context context;
    private String appPackageName;

    public Bitmap getBitmap(String resName) {
        if (!PkgUtils.isPkgInstalled(this.context, appPackageName)) {
            return BitmapFactory.decodeResource(this.context.getResources(), android.R.drawable.stat_sys_warning);
        }
        if (DEBUG) {
            Log.d(LOG_TAG, "getBitmap, resName: " + resName);
        }
        try {
            Context appContext = getAppContext();
            if (appContext != null) {
                Resources res = appContext.getResources();
                if (DEBUG) {
                    Log.d(LOG_TAG, "getBitmap, res: " + res);
                }
                if (res != null) {
                    int id = res.getIdentifier(resName, "drawable", appPackageName);
                    if (DEBUG) {
                        Log.d(LOG_TAG, "getBitmap, id: " + id);
                    }
                    if (id > 0) {
                        Bitmap bitmap = BitmapFactory.decodeResource(res, id);
                        if (DEBUG) {
                            Log.d(LOG_TAG, "getBitmap, bitmap: " + bitmap);
                        }
                        if (bitmap != null) {
                            return bitmap;
                        } else {
                            return BitmapFactory.decodeResource(this.context.getResources(), android.R.drawable.stat_sys_warning);
                        }
                    }
                }
            }
        } catch (Throwable e) {
            Log.e(LOG_TAG, "Fail getBitmap: " + Log.getStackTraceString(e));
        }
        return null;
    }

    public Icon getIcon(String resName) {
        return getIcon(resName, null);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public Icon getIcon(String resName, Transform<Bitmap> bitmapTransform) {
        if (!PkgUtils.isPkgInstalled(this.context, appPackageName)) {
            return Icon.createWithResource(this.context.getResources(), android.R.drawable.stat_sys_warning);
        }
        if (DEBUG) {
            Log.d(LOG_TAG, "getIcon, resName: " + resName);
        }
        try {
            Context appContext = getAppContext();
            if (appContext != null) {
                Resources res = appContext.getResources();
                if (DEBUG) {
                    Log.d(LOG_TAG, "getIcon, res: " + res);
                }
                if (res != null) {
                    int id = res.getIdentifier(resName, "drawable", appPackageName);
                    if (DEBUG) {
                        Log.d(LOG_TAG, "getIcon, id: " + id);
                    }
                    if (id > 0) {
                        Icon ic = null;
                        // Create with res directly.
                        if (bitmapTransform == null) {
                            ic = Icon.createWithResource(res, id);
                        } else {
                            // Create bitmap and transform.
                            try {
                                Bitmap icb = BitmapFactory.decodeResource(res, id);
                                if (icb != null) {
                                    icb = bitmapTransform.onTransform(icb);
                                    if (icb != null) {
                                        ic = Icon.createWithBitmap(icb);
                                    }
                                }
                            } catch (Exception e) {
                                Log.e(LOG_TAG, "getIcon, bitmap transform err: " + e);
                            } finally {
                                // Back to res.
                                if (ic == null) {
                                    ic = Icon.createWithResource(res, id);
                                }
                            }
                        }
                        if (DEBUG) {
                            Log.d(LOG_TAG, "getIcon, ic: " + ic);
                        }
                        if (ic != null) {
                            return ic;
                        }
                    }
                }
            }
        } catch (Throwable e) {
            Log.e(LOG_TAG, "Fail getIcon: " + Log.getStackTraceString(e));
        }
        return Icon.createWithResource(getContext(), android.R.drawable.stat_sys_warning);
    }

    private Context getAppContext() {
        Context context = getContext();
        if (context == null) {
            Log.e(LOG_TAG, "Context is null!!!");
            return null;
        }
        try {
            return context.createPackageContext(appPackageName, CONTEXT_IGNORE_SECURITY);
        } catch (Throwable e) {
            Log.e(LOG_TAG, "Fail createPackageContext: " + Log.getStackTraceString(e));
        }
        return null;
    }

    public String[] getStringArray(String resName) {
        if (!PkgUtils.isPkgInstalled(this.context, appPackageName)) {
            if (S_STRING_ARRAY_RES_CACHE.containsKey(resName)) {
                String[] cached = S_STRING_ARRAY_RES_CACHE.get(resName);
                if (cached != null) {
                    return cached;
                }
            }
            return new String[0];
        }
        Context context = getContext();
        if (context == null) {
            Log.e(LOG_TAG, "Context is null!!!");
            return new String[0];
        }
        try {
            Context appContext =
                    context.createPackageContext(appPackageName, CONTEXT_IGNORE_SECURITY);
            Resources res = appContext.getResources();
            int id = res.getIdentifier(resName, "array", appPackageName);
            Log.d(LOG_TAG, "getStringArray get id: " + id + ", for res: " + resName);
            if (id != 0) {
                String[] stringArr = res.getStringArray(id);
                S_STRING_ARRAY_RES_CACHE.put(resName, stringArr);
                return stringArr;
            }
        } catch (Throwable e) {
            Log.e(LOG_TAG, "Fail createPackageContext: " + Log.getStackTraceString(e));
        }
        return new String[0];
    }

    public String getString(String resName, Object... args) {
        if (!PkgUtils.isPkgInstalled(this.context, appPackageName)) {
            // Return cache.
            String cachedString = S_STRING_RES_CACHE.get(resName);
            if (cachedString != null) {
                return String.format(cachedString, args);
            }
            return resName;
        }
        Context context = getContext();
        if (context == null) {
            Log.e(LOG_TAG, "Context is null!!!");
            return null;
        }
        try {
            Context appContext =
                    context.createPackageContext(appPackageName, CONTEXT_IGNORE_SECURITY);
            Resources res = appContext.getResources();
            int id = res.getIdentifier(resName, "string", appPackageName);
            Log.d(LOG_TAG, "getString get id: " + id + ", for res: " + resName);
            if (id != 0) {
                String string = res.getString(id, args);
                S_STRING_RES_CACHE.put(resName, string);
                return string;
            }
        } catch (Throwable e) {
            Log.e(LOG_TAG, "Fail createPackageContext: " + Log.getStackTraceString(e));
        }
        return null;
    }

    public interface Transform<T> {
        T onTransform(T in) throws Exception;
    }
}
