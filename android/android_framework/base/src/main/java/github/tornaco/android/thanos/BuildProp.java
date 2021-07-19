package github.tornaco.android.thanos;

import android.util.Log;

import com.elvishew.xlog.XLog;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

public class BuildProp {
    private static final String PREFIX = "META-INF/";
    private static final Properties PROP = new Properties();
    private static final AtomicBoolean LOADED = new AtomicBoolean(false);

    // Build info.
    public static final String THANOS_VERSION_CODE = getString("thanox.build.version.code");
    public static final String THANOS_VERSION_NAME = getString("thanox.build.version.name");
    public static final String THANOS_BUILD_VARIANT = getString("thanox.build.variant");
    public static final Boolean THANOS_BUILD_DEBUG = getBoolean("thanox.build.debuggable", false);
    public static final String THANOS_BUILD_FLAVOR = getString("thanox.build.flavor");
    public static final String THANOS_BUILD_HOST = getString("thanox.build.host");
    public static final String THANOS_BUILD_FINGERPRINT = getString("thanox.build.fp");
    public static final String THANOS_BUILD_DATE = getString("thanox.build.date");

    // Package info
    public static final String THANOS_APP_PKG_NAME = getString("thanox.build.app.package.name");
    public static final String THANOS_APP_PKG_NAME_PREFIX = getString("thanox.build.app.package.name.prefix");
    public static final String THANOS_SHORTCUT_PKG_NAME_PREFIX = getString("thanox.build.shortcut.package.name.prefix");


    // Components
    public static final String ACTIVITY_APP_LOCK_VERIFIER = getString("locker.verify.activity");
    public static final String ACTIVITY_APP_LOCK_SETTINGS = getString("locker.settings.activity");
    public static final String ACTION_APP_LOCK = getString("locker.intent.action");
    public static final String ACTIVITY_APP_DETAILS = getString("thanox.app.details.activity");
    public static final String SU_SERVICE_CLASS = getString("thanox.app.su.service");

    // Default prefs
    public static final Boolean PREF_DEFAULT_BY_PASS_SYSTEM_APPS_ENABLED = getBoolean("pref.def.bypass.system.app.enabled", true);
    public static final Boolean PREF_DEFAULT_START_BLOCKER_ENABLED = getBoolean("pref.def.start.blocker.enabled", false);
    public static final Boolean PREF_DEFAULT_BG_RESTRICT_ENABLED = getBoolean("pref.def.background.restrict.enabled", false);


    public static final String ACTION_PERMISSION_REQUEST = getString("action.permissions.request");
    public static final String EXTRA_PERMISSION_REQUEST = getString("extra.permissions.request");

    // Urls
    public static final String THANOX_URL_DOCS_PROFILE = getString("thanox.url.docs.profile");
    public static final String THANOX_URL_DOCS_START_RULES = getString("thanox.url.docs.start_rule");
    public static final String THANOX_URL_PLAY_VERSION = getString("thanox.url.play.pro");
    public static final String THANOX_URL_DOCS_HOME = getString("thanox.url.doc.home");

    // Feats
    public static final String THANOX_FEATURE_PROFILE_A11Y = getString("thanox.feature.profile.accessibility");
    public static final String THANOX_FEATURE_PROFILE = getString("thanox.feature.profile");
    public static final String THANOX_FEATURE_APP_SMART_STAND_BY = getString("thanox.feature.app.smart_standby");
    public static final String THANOX_FEATURE_APP_TRAMPOLINE = getString("thanox.feature.app.trampoline");
    public static final String THANOX_FEATURE_APP_SMART_SERVICE_STOPPER = getString("thanox.feature.app.smart_service_stopper");
    public static final String THANOX_FEATURE_PUSH_DELEGATE = getString("thanox.feature.push.delegate");
    public static final String THANOX_FEATURE_PRIVACY_FIELD_IMEI = getString("thanox.feature.privacy.field.imei");
    public static final String THANOX_FEATURE_PRIVACY_FIELD_MEID = getString("thanox.feature.privacy.field.meid");
    public static final String THANOX_FEATURE_COMPONENT_MANAGER = getString("thanox.feature.component.manager");
    public static final String THANOX_FEATURE_START_BLOCKER = getString("thanox.feature.start.blocker");
    public static final String THANOX_FEATURE_RECENT_TASK_REMOVAL = getString("thanox.feature.recent.task.removal");
    public static final String THANOX_FEATURE_BG_TASK_CLEAN = getString("thanox.feature.background.task.clean");
    public static final String THANOX_FEATURE_PRIVACY_DATA_CHEAT = getString("thanox.feature.privacy.data.cheat");
    public static final String THANOX_FEATURE_PRIVACY_OPS = getString("thanox.feature.privacy.ops");
    public static final String THANOX_FEATURE_PRIVACY_APPLOCK = getString("thanox.feature.privacy.applock");
    public static final String THANOX_FEATURE_PRIVACY_OPS_REMINDER = getString("thanox.feature.privacy.ops.reminder");
    public static final String THANOX_FEATURE_PRIVACY_TASK_BLUR = getString("thanox.feature.privacy.task.blur");
    public static final String THANOX_FEATURE_EXT_N_UP = getString("thanox.feature.ext.n.up");
    public static final String THANOX_FEATURE_EXT_N_RECORDER = getString("thanox.feature.ext.n.recorder");
    public static final String THANOX_FEATURE_EXT_APP_SMART_FREEZE = getString("thanox.feature.ext.app.smart.freeze");
    public static final String THANOX_FEATURE_PLUGIN_SUPPORT = getString("thanox.feature.plugin.support");


    public static final String THANOX_CONTRIBUTORS = getString("thanox.dev.contributors");
    public static final String THANOX_CONTACT_EMAIL = getString("thanox.contact.email");

    // Base url.
    public static final String THANOX_SERVER_BASE_URL = getString("thanox.server.base.url");
    public static final String THANOX_SERVER_GITHUB_BASE_URL = getString("thanox.server.github.base.url");

    // Sec.
    public static final String THANOX_APP_PRC_SIGN_SEC = getString("thanox.prc.app.signature.sec");
    public static final String THANOX_APP_ROW_PLAY_SIGN_SEC = getString("thanox.row.play.app.signature.sec");
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

}
