-optimizationpasses 5
-dontusemixedcaseclassnames
#-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose

# Keep Annotations
-keepattributes *Annotation*
-keepattributes InnerClasses
-keepattributes Signature
-keepattributes Exceptions
-keepattributes SourceFile, LineNumberTable

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}


-keep class github.tornaco.android.thanox.magisk.bridge.** {*;}
-keepnames class github.tornaco.android.thanox.magisk.bridge.** {*;}

-keep public class * extends github.tornaco.android.thanox.magisk.bridge.proxy.BinderProxy { *; }
-keep class * implements github.tornaco.android.thanox.magisk.bridge.proxy.ExceptionTransformedInvocationHandler { *; }
-keep class * implements github.tornaco.android.thanox.magisk.bridge.proxy.BinderProxy$InvocationHandler { *; }
-keep class * implements java.lang.reflect.InvocationHandler { *; }


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
-dontskipnonpubliclibraryclassmembers
-printconfiguration
-keep,allowobfuscation @interface android.support.annotation.Keep

-keep @android.support.annotation.Keep class *
-keepclassmembers class * {
    @android.support.annotation.Keep *;
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

-keep class * implements android.os.Parcelable { *; }

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

# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature
-keepattributes Signature
-dontwarn android.support.**
-dontwarn com.squareup.**
-dontwarn okio.**
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule

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

-keep class org.slf4j.** {*;}
-keep class org.apache.commons.** {*;}
-keep class github.tornaco.android.thanos.BuildProp {*;}


# Please add these rules to your existing keep rules in order to suppress warnings.
# This is generated automatically by the Android Gradle plugin.
-dontwarn android.app.ActivityTaskManager
-dontwarn android.app.ActivityThread
-dontwarn android.app.AppGlobals
-dontwarn android.app.IActivityManager$Stub
-dontwarn android.app.IActivityManager
-dontwarn android.app.IActivityTaskManager$Stub
-dontwarn android.app.IActivityTaskManager
-dontwarn android.app.INotificationManager$Stub
-dontwarn android.app.INotificationManager
-dontwarn android.app.SystemServiceRegistry$ContextAwareServiceProducerWithoutBinder
-dontwarn android.app.SystemServiceRegistry
-dontwarn android.content.pm.PackageParser$PackageLite
-dontwarn android.content.pm.PackageParser$PackageParserException
-dontwarn android.content.pm.PackageParser
-dontwarn android.content.pm.ParceledListSlice
-dontwarn android.os.BinderProxy
-dontwarn android.os.ServiceManager$ServiceNotFoundException
-dontwarn android.os.ServiceManager
-dontwarn android.util.Singleton
-dontwarn com.android.internal.app.IAppOpsService$Stub
-dontwarn com.android.internal.app.IAppOpsService
-dontwarn com.android.internal.appwidget.IAppWidgetHost
-dontwarn com.android.internal.appwidget.IAppWidgetService$Stub
-dontwarn com.android.internal.appwidget.IAppWidgetService

-dontwarn de.robv.android.xposed.XposedBridge