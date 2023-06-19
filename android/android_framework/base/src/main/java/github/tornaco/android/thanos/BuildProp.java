package github.tornaco.android.thanos;

import android.util.Log;

import com.elvishew.xlog.XLog;

import java.io.IOException;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;


public class BuildProp {
    private static final String PREFIX = "META-INF/";
    private static final Properties PROP = new Properties();
    private static final AtomicBoolean LOADED = new AtomicBoolean(false);

    // Build info.
    @Dynamic
    public static final String THANOS_VERSION_CODE = getString("thanox.build.version.code");
    @Dynamic
    public static final String THANOS_VERSION_NAME = getString("thanox.build.version.name");
    @Dynamic
    public static final String THANOS_BUILD_VARIANT = getString("thanox.build.variant");
    @Dynamic
    public static final Boolean THANOS_BUILD_DEBUG = getBoolean("thanox.build.debuggable", false);
    @Dynamic
    public static final String THANOS_BUILD_FLAVOR = getString("thanox.build.flavor");
    @Dynamic
    public static final String THANOS_BUILD_HOST = getString("thanox.build.host");
    @Dynamic
    public static final String THANOS_BUILD_FINGERPRINT = getString("thanox.build.fp");
    @Dynamic
    public static final String THANOS_BUILD_DATE = getString("thanox.build.date");


    @Dynamic
    public static final String THANOS_APP_PKG_NAME = getString("thanox.build.app.package.name");

    public static final String THANOS_APP_PKG_NAME_PREFIX = "github.tornaco.android.thanos";
    public static final String THANOS_SHORTCUT_PKG_NAME_PREFIX = "github.tornaco.android.thanos.shortcut";
    public static final String SHORTX_APP_PKG_NAME = "tornaco.apps.shortx";

    // Components
    @Dynamic
    public static final String ACTIVITY_APP_LOCK_VERIFIER = getString("locker.verify.activity");
    @Dynamic
    public static final String ACTIVITY_APP_LOCK_SETTINGS = getString("locker.settings.activity");
    @Dynamic
    public static final String ACTION_APP_LOCK = getString("locker.intent.action");
    @Dynamic
    public static final String ACTIVITY_APP_DETAILS = getString("thanox.app.details.activity");
    @Dynamic
    public static final String SU_SERVICE_CLASS = getString("thanox.app.su.service");

    // Default prefs
    @Dynamic
    public static final Boolean PREF_DEFAULT_BY_PASS_SYSTEM_APPS_ENABLED = getBoolean("pref.def.bypass.system.app.enabled", true);
    @Dynamic
    public static final Boolean PREF_DEFAULT_START_BLOCKER_ENABLED = getBoolean("pref.def.start.blocker.enabled", false);
    @Dynamic
    public static final Boolean PREF_DEFAULT_BG_RESTRICT_ENABLED = getBoolean("pref.def.background.restrict.enabled", false);


    @Dynamic
    public static final String ACTION_PERMISSION_REQUEST = getString("action.permissions.request");
    @Dynamic
    public static final String EXTRA_PERMISSION_REQUEST = getString("extra.permissions.request");

    // Urls
    public static final String THANOX_URL_DOCS_PROFILE = getString("thanox.url.docs.profile");
    public static final String THANOX_URL_DOCS_START_RULES = getString("thanox.url.docs.start_rule");
    public static final String THANOX_URL_PLAY_VERSION = getString("thanox.url.play.pro");
    public static final String THANOX_URL_DOCS_HOME = getString("thanox.url.doc.home");

    // Feats
    public static final String THANOX_FEATURE_PROFILE_A11Y = "thanox.feature.profile.accessibility";
    public static final String THANOX_FEATURE_PROFILE = "thanox.feature.profile";
    public static final String THANOX_FEATURE_APP_SMART_STAND_BY = "thanox.feature.app.smart_standby";
    public static final String THANOX_FEATURE_APP_TRAMPOLINE = "thanox.feature.app.trampoline";
    public static final String THANOX_FEATURE_APP_SMART_SERVICE_STOPPER = "thanox.feature.app.smart_service_stopper";
    public static final String THANOX_FEATURE_PUSH_DELEGATE = "thanox.feature.push.delegate";
    public static final String THANOX_FEATURE_PRIVACY_FIELD_IMEI = "thanox.feature.privacy.field.imei";
    public static final String THANOX_FEATURE_PRIVACY_FIELD_MEID = "thanox.feature.privacy.field.meid";
    public static final String THANOX_FEATURE_COMPONENT_MANAGER = "thanox.feature.component.manager";
    public static final String THANOX_FEATURE_START_BLOCKER = "thanox.feature.start.blocker";
    public static final String THANOX_FEATURE_RECENT_TASK_REMOVAL = "thanox.feature.recent.task.removal";
    public static final String THANOX_FEATURE_RECENT_TASK_FORCE_EXCLUDE = "thanox.feature.recent.force.exclude";
    public static final String THANOX_FEATURE_RECENT_TASK_FORCE_INCLUDE = "thanox.feature.recent.force.include";
    public static final String THANOX_FEATURE_BG_TASK_CLEAN = "thanox.feature.background.task.clean";
    public static final String THANOX_FEATURE_PRIVACY_DATA_CHEAT = "thanox.feature.privacy.data.cheat";
    public static final String THANOX_FEATURE_PRIVACY_OPS = "thanox.feature.privacy.ops";
    public static final String THANOX_FEATURE_PRIVACY_APPLOCK = "thanox.feature.privacy.applock";
    public static final String THANOX_FEATURE_PRIVACY_OPS_REMINDER = "thanox.feature.privacy.ops.reminder";
    public static final String THANOX_FEATURE_PRIVACY_TASK_BLUR = "thanox.feature.privacy.task.blur";
    public static final String THANOX_FEATURE_EXT_N_UP = "thanox.feature.ext.n.up";
    public static final String THANOX_FEATURE_EXT_N_RECORDER = "thanox.feature.ext.n.recorder";
    public static final String THANOX_FEATURE_EXT_N_RECORDER_CLIPBOARD = "thanox.feature.ext.n.recorder.clipboard";
    public static final String THANOX_FEATURE_EXT_APP_SMART_FREEZE = "thanox.feature.ext.app.smart.freeze";
    public static final String THANOX_FEATURE_PLUGIN_SUPPORT = "thanox.feature.plugin.support";
    public static final String THANOX_FEATURE_PREVENT_UNINSTALL = "thanox.feature.prevent.uninstall";
    public static final String THANOX_FEATURE_PREVENT_CLEAR_DATA = "thanox.feature.prevent.clear.data";
    public static final String THANOX_FEATURE_DIALOG_FORCE_CANCELABLE = "thanox.feature.dialog.force.cancelable";
    public static final String THANOX_FEATURE_WAKELOCK_REMOVER = "thanox.feature.dialog.wakelock.remover";
    public static final String THANOX_FEATURE_IZ = "thanox.feature.iz";
    public static final String THANOX_FEATURE_RESIDENT = "thanox.feature.resident";

    public static final String THANOX_CONTRIBUTORS = getString("thanox.dev.contributors");
    public static final String THANOX_CONTACT_EMAIL = getString("thanox.contact.email");
    public static final String THANOX_TG_CHANNEL = getString("thanox.tg");
    public static final String THANOX_QQ_PRIMARY = getString("thanox.qq.primary");

    // Base url.
    @Dynamic
    public static final String THANOX_SERVER_BASE_URL = getString("thanox.server.base.url");
    @Dynamic
    public static final String THANOX_SERVER_GITHUB_BASE_URL = getString("thanox.server.github.base.url");

    // Sec.
    @Dynamic
    public static final String THANOX_APP_PRC_SIGN_SEC = getString("thanox.prc.app.signature.sec");
    @Dynamic
    public static final String THANOX_APP_ROW_PLAY_SIGN_SEC = getString("thanox.row.play.app.signature.sec");
    @Dynamic
    public static final String THANOX_APP_ROW_PLAY_PUBLIC_KEY = getString("thanox.row.play.service.public.key");


    public static String getString(String key) {
        ensureLoaded();
        return PROP.getProperty(key);
    }

    public static String getString(String key, String defaultValue) {
        ensureLoaded();
        return PROP.getProperty(key, defaultValue);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return Boolean.parseBoolean(getString(key, Boolean.toString(defaultValue)));
    }

    private static void ensureLoaded() {
        if (LOADED.compareAndSet(false, true)) {
            XLog.i("ThanoxProp, Load prop now.");
            try {
                PROP.load(BuildProp.class.getClassLoader().getResourceAsStream(PREFIX + "thanos.properties"));
            } catch (IOException e) {
                XLog.e("ThanoxProp load error " + Log.getStackTraceString(e));
            }
        }
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface Dynamic {
    }
}
