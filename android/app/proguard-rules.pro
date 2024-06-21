-repackageclasses fortuitous

-optimizationpasses 5
-dontusemixedcaseclassnames
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

# Keep Annotations
-keepattributes *Annotation*

# Keep InnerClasses
# https://stackoverflow.com/questions/52252806/android-build-error-attribute-signature-requires-innerclasses-attribute-check
-keepattributes InnerClasses

# Gson uses generic type information stored in a class file when working with
# fields. Proguard removes such information by default, keep it.
-keepattributes Signature

# This is also needed for R8 in compat mode since multiple
# optimizations will remove the generic signature such as class
# merging and argument removal. See:
# https://r8.googlesource.com/r8/+/refs/heads/main/compatibility-faq.md#troubleshooting-gson-gson
-keep class com.google.gson.reflect.TypeToken { *; }
-keep class * extends com.google.gson.reflect.TypeToken

-keep public class * extends android.app.Activity
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference

# @Keep
# http://tools.android.com/tech-docs/support-annotations
-printconfiguration
-keep,allowobfuscation @interface android.support.annotation.Keep

-keep @android.support.annotation.Keep class *
-keepclassmembers class * {
    @android.support.annotation.Keep *;
}

-keep,allowobfuscation @interface github.tornaco.android.thanos.core.annotation.DoNotStrip

# Do not strip any method/class that is annotated with @DoNotStrip
-keep,allowobfuscation @interface github.tornaco.android.thanos.core.annotation.DoNotStrip
-keep @github.tornaco.android.thanos.core.annotation.DoNotStrip class * {*;}

-keepclasseswithmembers class * {
    @github.tornaco.android.thanos.core.annotation.DoNotStrip <methods>;
}

-keepclasseswithmembers class * {
    @github.tornaco.android.thanos.core.annotation.DoNotStrip <fields>;
}

-keepclasseswithmembers class * {
    @github.tornaco.android.thanos.core.annotation.DoNotStrip <init>(...);
}

# Supports
-keep class android.support.** { *; }
-keep interface android.support.** { *; }

# keep setters in Views so that animations can still work.
# see http://proguard.sourceforge.net/manual/examples.html#beans
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keep class **.R$* {
 *;
}
-keepclassmembers class * {
    void *(**On*Event);
}

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature
-keepattributes Signature
-dontwarn android.support.**
-dontwarn com.squareup.**
-dontwarn okio.**
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule

# Keep our interfaces so they can be used by other ProGuard rules.
# See http://sourceforge.net/p/proguard/bugs/466/
-keep,allowobfuscation @interface com.facebook.crypto.proguard.annotations.DoNotStrip
-keep,allowobfuscation @interface com.facebook.crypto.proguard.annotations.KeepGettersAndSetters

# Do not strip any method/class that is annotated with @DoNotStrip
-keep @com.facebook.crypto.proguard.annotations.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.crypto.proguard.annotations.DoNotStrip *;
}

-keepclassmembers @com.facebook.crypto.proguard.annotations.KeepGettersAndSetters class * {
  void set*(***);
  *** get*();
}


# Configuration for Guava 18.0
#
# disagrees with instructions provided by Guava project: https://code.google.com/p/guava-libraries/wiki/UsingProGuardWithGuava

-keep class com.google.common.io.Resources {
    public static <methods>;
}
-keep class com.google.common.collect.Lists {
    public static ** reverse(**);
}
-keep class com.google.common.base.Charsets {
    public static <fields>;
}

-keep class com.google.common.base.Joiner {
    public static com.google.common.base.Joiner on(java.lang.String);
    public ** join(...);
}

-keep class com.google.common.collect.MapMakerInternalMap$ReferenceEntry
-keep class com.google.common.cache.LocalCache$ReferenceEntry

# http://stackoverflow.com/questions/9120338/proguard-configuration-for-guava-with-obfuscation-and-optimization
-dontwarn javax.annotation.**
-dontwarn javax.inject.**
-dontwarn sun.misc.Unsafe

# Guava 19.0
-dontwarn java.lang.ClassValue
-dontwarn com.google.j2objc.annotations.Weak
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

-dontwarn com.google.common.**

# Glide http://bumptech.github.io/glide/doc/download-setup.html#proguard
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# If you're targeting any API level less than Android API 27, also include:
-dontwarn com.bumptech.glide.load.resource.bitmap.VideoDecoder

# If you do not use Rx:
-dontwarn rx.**

# XposedEntry
-keep class github.tornaco.android.thanos.services.xposed.XposedHookEntry
-keep class github.tornaco.android.thanos.services.xposed.LSPosedHookEntry
-keep class github.tornaco.android.thanos.core** {*;}
-keep class github.tornaco.android.thanos.services** {*;}
-keep class github.tornaco.android.thanos.db** {*;}
-keep class github.tornaco.android.thanox.magisk** {*;}
# github.tornaco.thanox.android.server.patch.framework
-keep class github.tornaco.thanox.android.server** {*;}
-keep class github.tornaco.xposed.patchx** {*;}
-keep class ** implements github.tornaco.android.thanos.core.persist** { *; }

-keep class io.github.libxposed.service** {*;}

# Slf
-keep class org.slf4j.** {*;}

-keep class org.apache.commons.** {*;}

# For debug purpose
-keep class com.bumptech.glide.** {*;}

# Keep utils.
-keep class util.** {*;}

-keep class github.tornaco.android.thanos.BuildProp {*;}
-keep class now.fortuitous.wm.UiAutomationManager {*;}


-keep class androidx.databinding.** {*;}
# keep setters in Views so that animations can still work.
# see http://proguard.sourceforge.net/manual/examples.html#beans
-keep class * extends androidx.databinding.DataBinderMapper {*;}

-keep class org.jeasy.rules.core.** {*;}
-keep class org.mvel2.** {*;}
-dontwarn org.mvel2.**


-keep class io.github.libxposed.** { *; }
-keep class com.topjohnwu.superuser.** { *; }

# With R8 full mode generic signatures are stripped for classes that are not
# kept. Suspend functions are wrapped in continuations where the type argument
# is used.
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response

# R8 full mode strips generic signatures from return types if not kept.
-if interface * { @retrofit2.http.* public *** *(...); }
-keep,allowoptimization,allowshrinking,allowobfuscation class <3>


# Please add these rules to your existing keep rules in order to suppress warnings.
# This is generated automatically by the Android Gradle plugin.
-dontwarn android.accessibilityservice.IAccessibilityServiceClient$Stub
-dontwarn android.accessibilityservice.IAccessibilityServiceClient
-dontwarn android.accessibilityservice.IAccessibilityServiceConnection
-dontwarn android.app.ActivityManager$TaskSnapshot
-dontwarn android.app.ActivityManagerInternal
-dontwarn android.app.ActivityThread
-dontwarn android.app.AppOpsManager$OpEntry
-dontwarn android.app.AppOpsManager$PackageOps
-dontwarn android.app.ContextImpl
-dontwarn android.app.IActivityManager$Stub
-dontwarn android.app.IActivityManager
-dontwarn android.app.IApplicationThread
-dontwarn android.app.INotificationManager
-dontwarn android.app.LoadedApk
-dontwarn android.app.PackageDeleteObserver
-dontwarn android.app.UiAutomationConnection
-dontwarn android.app.usage.IUsageStatsManager$Stub
-dontwarn android.app.usage.IUsageStatsManager
-dontwarn android.content.IIntentSender$Stub
-dontwarn android.content.IIntentSender
-dontwarn android.content.pm.IPackageInstaller
-dontwarn android.content.pm.IPackageManager$Stub
-dontwarn android.content.pm.IPackageManager
-dontwarn android.content.pm.PackageParser$Activity
-dontwarn android.content.pm.PackageParser$Instrumentation
-dontwarn android.content.pm.PackageParser$Provider
-dontwarn android.content.pm.PackageParser$Service
-dontwarn android.content.pm.ParceledListSlice
-dontwarn android.content.pm.SuspendDialogInfo
-dontwarn android.content.pm.UserInfo
-dontwarn android.graphics.GraphicBuffer
-dontwarn android.net.INetworkScoreCache
-dontwarn android.net.NetworkKey
-dontwarn android.net.NetworkScoreManager
-dontwarn android.net.ScoredNetwork
-dontwarn android.net.wifi.WifiNetworkScoreCache$CacheListener
-dontwarn android.net.wifi.WifiNetworkScoreCache
-dontwarn android.os.IProgressListener$Stub
-dontwarn android.os.IProgressListener
-dontwarn android.os.RemoteCallback$OnResultListener
-dontwarn android.os.RemoteCallback
-dontwarn android.os.ServiceManager
-dontwarn android.os.ShellCallback
-dontwarn android.os.SystemProperties
-dontwarn android.util.Slog
-dontwarn android.view.accessibility.AccessibilityInteractionClient
-dontwarn android.webkit.IWebViewUpdateService$Stub
-dontwarn android.webkit.IWebViewUpdateService
-dontwarn android.webkit.WebViewProviderInfo
-dontwarn com.android.internal.annotations.GuardedBy
-dontwarn com.android.internal.app.IAppOpsService$Stub
-dontwarn com.android.internal.app.IAppOpsService
-dontwarn com.android.internal.appwidget.IAppWidgetHost
-dontwarn com.android.internal.appwidget.IAppWidgetService$Stub
-dontwarn com.android.internal.appwidget.IAppWidgetService
-dontwarn com.android.internal.os.ProcLocksReader
-dontwarn com.android.internal.util.FastPrintWriter
-dontwarn com.android.internal.util.FastXmlSerializer
-dontwarn com.android.server.LocalServices
-dontwarn com.android.server.SystemConfig
-dontwarn github.tornaco.xposed.annotation.XposedHook
-dontwarn io.github.libxposed.api.XposedInterface
-dontwarn io.github.libxposed.api.XposedModule
-dontwarn io.github.libxposed.api.XposedModuleInterface$ModuleLoadedParam
-dontwarn io.github.libxposed.api.XposedModuleInterface$PackageLoadedParam
-dontwarn io.github.libxposed.api.XposedModuleInterface$SystemServerLoadedParam
-dontwarn java.beans.BeanInfo
-dontwarn java.beans.FeatureDescriptor
-dontwarn java.beans.IntrospectionException
-dontwarn java.beans.Introspector
-dontwarn java.beans.PropertyDescriptor
-dontwarn lombok.NonNull



# Proto
-keep class tornaco.apps.thanox.core.proto** {*;}
# Exclude protobuf classes
-keep class com.google.protobuf.** { *; }


-keep,allowobfuscation @interface github.tornaco.android.thanos.core.DoNotStrip

# Do not strip any method/class that is annotated with @DoNotStrip
-keep,allowobfuscation @interface github.tornaco.android.thanos.core.DoNotStrip
-keep @github.tornaco.android.thanos.core.DoNotStrip class * {*;}

-keepclasseswithmembers class * {
    @github.tornaco.android.thanos.core.DoNotStrip <methods>;
}

-keepclasseswithmembers class * {
    @github.tornaco.android.thanos.core.DoNotStrip <fields>;
}

-keepclasseswithmembers class * {
    @github.tornaco.android.thanos.core.DoNotStrip <init>(...);
}

# java.lang.VerifyError: Verifier rejected class t7.e0: void t7.e0.b(int, int[])
# failed to verify: void t7.e0.b(int, int[]): [0x7] register v5 has type Precise Reference:
# int[] but expected Integer (declaration of 't7.e0' appears in Anonymous-DexFile@3310950862)
-keep class com.squareup.** { *; }

# Keep thanox core.
-keep class github.tornaco.android.thanos.core** {*;}

# Retain generic signatures of TypeToken and its subclasses with R8 version 3.0 and higher.
-keep,allowobfuscation,allowshrinking class com.google.gson.reflect.TypeToken
-keep,allowobfuscation,allowshrinking class * extends com.google.gson.reflect.TypeToken

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# https://github.com/thegrizzlylabs/sardine-android/issues/70
-dontwarn org.xmlpull.v1.**
-keep class org.xmlpull.v1.** { *; }
-dontwarn android.content.res.XmlResourceParser

-dontwarn android.content.pm.ParceledListSlice
-dontwarn android.os.ServiceManager
-dontwarn com.android.internal.appwidget.IAppWidgetHost
-dontwarn com.android.internal.appwidget.IAppWidgetService$Stub
-dontwarn com.android.internal.appwidget.IAppWidgetService
-dontwarn android.app.ActivityThread
-dontwarn android.app.IActivityManager$Stub
-dontwarn android.app.IActivityManager
-dontwarn android.app.IApplicationThread
-dontwarn android.app.ITaskStackListener
-dontwarn android.app.ProfilerInfo
-dontwarn android.app.TaskStackListener
-dontwarn android.content.pm.IPackageManager$Stub
-dontwarn android.content.pm.IPackageManager
-dontwarn android.content.pm.SuspendDialogInfo
-dontwarn android.content.pm.UserInfo
-dontwarn android.os.IUserManager$Stub
-dontwarn android.os.IUserManager
-dontwarn android.util.Slog
-dontwarn android.app.IProcessObserver$Stub
-dontwarn android.app.IProcessObserver
-dontwarn com.android.internal.annotations.GuardedBy
-dontwarn com.android.internal.annotations.VisibleForTesting