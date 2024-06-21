-repackageclasses thanox

# https://github.com/square/retrofit/issues/3751
# Keep generic signature of Call, Response (R8 full mode strips signatures from non-kept items).
 -keep,allowobfuscation,allowshrinking interface retrofit2.Call
 -keep,allowobfuscation,allowshrinking class retrofit2.Response
# With R8 full mode generic signatures are stripped for classes that are not
# kept. Suspend functions are wrapped in continuations where the type argument
# is used.
 -keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

# Proto
-keep class tornaco.apps.thanox.core.proto** {*;}
# Exclude protobuf classes
-keep class com.google.protobuf.** { *; }

-keep class ** implements android.os.Parcelable { *; }
-keep class ** implements android.os.IInterface { *; }

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