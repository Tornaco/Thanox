package io.github.libxposed.api;

import android.annotation.NonNull;
import android.annotation.Nullable;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.ConcurrentModificationException;

import io.github.libxposed.api.errors.HookFailedError;
import io.github.libxposed.api.utils.DexParser;

/**
 * Xposed interface for modules to operate on application processes.
 */
@SuppressWarnings("unused")
public interface XposedInterface {
    /**
     * SDK API version.
     */
    int API = 100;

    /**
     * Indicates that the framework is running as root.
     */
    int FRAMEWORK_PRIVILEGE_ROOT = 0;
    /**
     * Indicates that the framework is running in a container with a fake system_server.
     */
    int FRAMEWORK_PRIVILEGE_CONTAINER = 1;
    /**
     * Indicates that the framework is running as a different app, which may have at most shell permission.
     */
    int FRAMEWORK_PRIVILEGE_APP = 2;
    /**
     * Indicates that the framework is embedded in the hooked app,
     * which means {@link #getSharedPreferences} will be null and remote file is unsupported.
     */
    int FRAMEWORK_PRIVILEGE_EMBEDDED = 3;

    /**
     * The default hook priority.
     */
    int PRIORITY_DEFAULT = 50;
    /**
     * Execute the hook callback late.
     */
    int PRIORITY_LOWEST = -10000;
    /**
     * Execute the hook callback early.
     */
    int PRIORITY_HIGHEST = 10000;

    /**
     * The interface Before hook callback.
     *
     * @param <T> the type parameter
     */
    interface BeforeHookCallback<T> {
        /**
         * Gets origin.
         *
         * @return the origin
         */
        @NonNull
        T getOrigin();

        /**
         * Gets this.
         *
         * @return the this
         */
        @Nullable
        Object getThis();

        /**
         * Get args object [ ].
         *
         * @return the object [ ]
         */
        @NonNull
        Object[] getArgs();

        /**
         * Gets arg.
         *
         * @param <U>   the type parameter
         * @param index the index
         * @return the arg
         */
        @Nullable
        <U> U getArg(int index);

        /**
         * Sets arg.
         *
         * @param <U>   the type parameter
         * @param index the index
         * @param value the value
         */
        <U> void setArg(int index, U value);

        /**
         * Return and skip.
         *
         * @param returnValue the return value
         */
        void returnAndSkip(@Nullable Object returnValue);

        /**
         * Throw and skip.
         *
         * @param throwable the throwable
         */
        void throwAndSkip(@Nullable Throwable throwable);

        /**
         * Invoke origin object.
         *
         * @return the object
         * @throws InvocationTargetException the invocation target exception
         * @throws IllegalArgumentException  the illegal argument exception
         * @throws IllegalAccessException    the illegal access exception
         */
        @Nullable
        Object invokeOrigin() throws InvocationTargetException, IllegalArgumentException, IllegalAccessException;

        /**
         * Sets extra.
         *
         * @param <U>   the type parameter
         * @param key   the key
         * @param value the value
         * @throws ConcurrentModificationException the concurrent modification exception
         */
        <U> void setExtra(@NonNull String key, @Nullable U value) throws ConcurrentModificationException;
    }

    /**
     * The interface After hook callback.
     *
     * @param <T> the type parameter
     */
    interface AfterHookCallback<T> {
        /**
         * Gets origin.
         *
         * @return the origin
         */
        @NonNull
        T getOrigin();

        /**
         * Gets this.
         *
         * @return the this
         */
        @Nullable
        Object getThis();

        /**
         * Get args object [ ].
         *
         * @return the object [ ]
         */
        @NonNull
        Object[] getArgs();

        /**
         * Gets result.
         *
         * @return the result
         */
        @Nullable
        Object getResult();

        /**
         * Gets throwable.
         *
         * @return the throwable
         */
        @Nullable
        Throwable getThrowable();

        /**
         * Is skipped boolean.
         *
         * @return the boolean
         */
        boolean isSkipped();

        /**
         * Sets result.
         *
         * @param result the result
         */
        void setResult(@Nullable Object result);

        /**
         * Sets throwable.
         *
         * @param throwable the throwable
         */
        void setThrowable(@Nullable Throwable throwable);

        /**
         * Invoke origin object.
         *
         * @return the object
         * @throws InvocationTargetException the invocation target exception
         * @throws IllegalAccessException    the illegal access exception
         */
        @Nullable
        Object invokeOrigin() throws InvocationTargetException, IllegalAccessException;

        /**
         * Gets extra.
         *
         * @param <U> the type parameter
         * @param key the key
         * @return the extra
         */
        @Nullable
        <U> U getExtra(@NonNull String key);
    }

    /**
     * The interface Before hooker.
     *
     * @param <T> the type parameter
     */
    interface BeforeHooker<T> {
        /**
         * Before.
         *
         * @param callback the callback
         */
        void before(@NonNull BeforeHookCallback<T> callback);
    }

    /**
     * The interface After hooker.
     *
     * @param <T> the type parameter
     */
    interface AfterHooker<T> {
        /**
         * After.
         *
         * @param callback the callback
         */
        void after(@NonNull AfterHookCallback<T> callback);
    }

    /**
     * The interface Hooker.
     *
     * @param <T> the type parameter
     */
    interface Hooker<T> extends BeforeHooker<T>, AfterHooker<T> {
    }

    /**
     * The interface Method unhooker.
     *
     * @param <T> the type parameter
     * @param <U> the type parameter
     */
    interface MethodUnhooker<T, U> {
        /**
         * Gets origin.
         *
         * @return the origin
         */
        @NonNull
        U getOrigin();

        /**
         * Gets hooker.
         *
         * @return the hooker
         */
        @NonNull
        T getHooker();

        /**
         * Unhook.
         */
        void unhook();
    }

    /**
     * Get the Xposed framework name of current implementation.
     *
     * @return Framework name
     */
    @NonNull
    String getFrameworkName();

    /**
     * Get the Xposed framework version of current implementation.
     *
     * @return Framework version
     */
    @NonNull
    String getFrameworkVersion();

    /**
     * Get the Xposed framework version code of current implementation.
     *
     * @return Framework version code
     */
    long getFrameworkVersionCode();

    /**
     * Get the Xposed framework privilege of current implementation.
     *
     * @return Framework privilege
     */
    int getFrameworkPrivilege();

    /**
     * Additional methods provided by specific Xposed framework.
     *
     * @param name Featured method name
     * @param args Featured method arguments
     * @return Featured method result
     * @throws UnsupportedOperationException If the framework does not provide a method with given name
     */
    // @Discouraged(message = "Normally, modules should never rely on specific implementation of the Xposed framework. But if really necessary, this method can be used to acquire such information.")
    @Nullable
    Object featuredMethod(String name, Object... args);

    /**
     * Hook before method unhooker.
     *
     * @param origin the origin
     * @param hooker the hooker
     * @return the method unhooker
     * @throws IllegalArgumentException if origin is abstract, framework internal or {@link Method#invoke}
     * @throws HookFailedError          if hook fails due to framework internal error
     */
    @NonNull
    MethodUnhooker<BeforeHooker<Method>, Method> hookBefore(@NonNull Method origin, @NonNull BeforeHooker<Method> hooker);

    /**
     * Hook after method unhooker.
     *
     * @param origin the origin
     * @param hooker the hooker
     * @return the method unhooker
     * @throws IllegalArgumentException if origin is abstract, framework internal or {@link Method#invoke}
     * @throws HookFailedError          if hook fails due to framework internal error
     */
    @NonNull
    MethodUnhooker<AfterHooker<Method>, Method> hookAfter(@NonNull Method origin, @NonNull AfterHooker<Method> hooker);

    /**
     * Hook method unhooker.
     *
     * @param origin the origin
     * @param hooker the hooker
     * @return the method unhooker
     * @throws IllegalArgumentException if origin is abstract, framework internal or {@link Method#invoke}
     * @throws HookFailedError          if hook fails due to framework internal error
     */
    @NonNull
    MethodUnhooker<Hooker<Method>, Method> hook(@NonNull Method origin, @NonNull Hooker<Method> hooker);

    /**
     * Hook before method unhooker.
     *
     * @param origin   the origin
     * @param priority the priority
     * @param hooker   the hooker
     * @return the method unhooker
     * @throws IllegalArgumentException if origin is abstract, framework internal or {@link Method#invoke}
     * @throws HookFailedError          if hook fails due to framework internal error
     */
    @NonNull
    MethodUnhooker<BeforeHooker<Method>, Method> hookBefore(@NonNull Method origin, int priority, @NonNull BeforeHooker<Method> hooker);

    /**
     * Hook after method unhooker.
     *
     * @param origin   the origin
     * @param priority the priority
     * @param hooker   the hooker
     * @return the method unhooker
     * @throws IllegalArgumentException if origin is abstract, framework internal or {@link Method#invoke}
     * @throws HookFailedError          if hook fails due to framework internal error
     */
    @NonNull
    MethodUnhooker<AfterHooker<Method>, Method> hookAfter(@NonNull Method origin, int priority, @NonNull AfterHooker<Method> hooker);

    /**
     * Hook method unhooker.
     *
     * @param origin   the origin
     * @param priority the priority
     * @param hooker   the hooker
     * @return the method unhooker
     * @throws IllegalArgumentException if origin is abstract, framework internal or {@link Method#invoke}
     * @throws HookFailedError          if hook fails due to framework internal error
     */
    @NonNull
    MethodUnhooker<Hooker<Method>, Method> hook(@NonNull Method origin, int priority, @NonNull Hooker<Method> hooker);

    /**
     * Hook before method unhooker.
     *
     * @param <T>    the type parameter
     * @param origin the origin
     * @param hooker the hooker
     * @return the method unhooker
     * @throws IllegalArgumentException if origin is abstract, framework internal or {@link Method#invoke}
     * @throws HookFailedError          if hook fails due to framework internal error
     */
    @NonNull
    <T> MethodUnhooker<BeforeHooker<Constructor<T>>, Constructor<T>> hookBefore(@NonNull Constructor<T> origin, @NonNull BeforeHooker<Constructor<T>> hooker);

    /**
     * Hook after method unhooker.
     *
     * @param <T>    the type parameter
     * @param origin the origin
     * @param hooker the hooker
     * @return the method unhooker
     * @throws IllegalArgumentException if origin is abstract, framework internal or {@link Method#invoke}
     * @throws HookFailedError          if hook fails due to framework internal error
     */
    @NonNull
    <T> MethodUnhooker<AfterHooker<Constructor<T>>, Constructor<T>> hookAfter(@NonNull Constructor<T> origin, @NonNull AfterHooker<Constructor<T>> hooker);

    /**
     * Hook method unhooker.
     *
     * @param <T>    the type parameter
     * @param origin the origin
     * @param hooker the hooker
     * @return the method unhooker
     * @throws IllegalArgumentException if origin is abstract, framework internal or {@link Method#invoke}
     * @throws HookFailedError          if hook fails due to framework internal error
     */
    @NonNull
    <T> MethodUnhooker<Hooker<Constructor<T>>, Constructor<T>> hook(@NonNull Constructor<T> origin, @NonNull Hooker<Constructor<T>> hooker);

    /**
     * Hook before method unhooker.
     *
     * @param <T>      the type parameter
     * @param origin   the origin
     * @param priority the priority
     * @param hooker   the hooker
     * @return the method unhooker
     * @throws IllegalArgumentException if origin is abstract, framework internal or {@link Method#invoke}
     * @throws HookFailedError          if hook fails due to framework internal error
     */
    @NonNull
    <T> MethodUnhooker<BeforeHooker<Constructor<T>>, Constructor<T>> hookBefore(@NonNull Constructor<T> origin, int priority, @NonNull BeforeHooker<Constructor<T>> hooker);

    /**
     * Hook after method unhooker.
     *
     * @param <T>      the type parameter
     * @param origin   the origin
     * @param priority the priority
     * @param hooker   the hooker
     * @return the method unhooker
     * @throws IllegalArgumentException if origin is abstract, framework internal or {@link Method#invoke}
     * @throws HookFailedError          if hook fails due to framework internal error
     */
    @NonNull
    <T> MethodUnhooker<AfterHooker<Constructor<T>>, Constructor<T>> hookAfter(@NonNull Constructor<T> origin, int priority, @NonNull AfterHooker<Constructor<T>> hooker);

    /**
     * Hook method unhooker.
     *
     * @param <T>      the type parameter
     * @param origin   the origin
     * @param priority the priority
     * @param hooker   the hooker
     * @return the method unhooker
     * @throws IllegalArgumentException if origin is abstract, framework internal or {@link Method#invoke}
     * @throws HookFailedError          if hook fails due to framework internal error
     */
    @NonNull
    <T> MethodUnhooker<Hooker<Constructor<T>>, Constructor<T>> hook(@NonNull Constructor<T> origin, int priority, @NonNull Hooker<Constructor<T>> hooker);

    /**
     * Deoptimize boolean.
     *
     * @param method the method
     * @return the boolean
     */
    boolean deoptimize(@NonNull Method method);

    /**
     * Deoptimize boolean.
     *
     * @param <T>         the type parameter
     * @param constructor the constructor
     * @return the boolean
     */
    <T> boolean deoptimize(@NonNull Constructor<T> constructor);

    /**
     * Invoke origin object.
     *
     * @param method     the method
     * @param thisObject the this object
     * @param args       the args
     * @return the object
     * @throws InvocationTargetException the invocation target exception
     * @throws IllegalArgumentException  the illegal argument exception
     * @throws IllegalAccessException    the illegal access exception
     */
    @Nullable
    Object invokeOrigin(@NonNull Method method, @Nullable Object thisObject, Object... args) throws InvocationTargetException, IllegalArgumentException, IllegalAccessException;

    /**
     * Invoke special object.
     *
     * @param method     the method
     * @param thisObject the this object
     * @param args       the args
     * @return the object
     * @throws InvocationTargetException the invocation target exception
     * @throws IllegalArgumentException  the illegal argument exception
     * @throws IllegalAccessException    the illegal access exception
     */
    @Nullable
    Object invokeSpecial(@NonNull Method method, @NonNull Object thisObject, Object... args) throws InvocationTargetException, IllegalArgumentException, IllegalAccessException;

    /**
     * new origin object.
     *
     * @param <T>         the type parameter
     * @param constructor the constructor
     * @param args        the args
     * @return the object
     * @throws InvocationTargetException the invocation target exception
     * @throws IllegalArgumentException  the illegal argument exception
     * @throws IllegalAccessException    the illegal access exception
     * @throws InstantiationException    the instantiation exception
     */
    @NonNull
    <T> T newInstanceOrigin(@NonNull Constructor<T> constructor, Object... args) throws InvocationTargetException, IllegalArgumentException, IllegalAccessException, InstantiationException;


    /**
     * New instance special u.
     *
     * @param <T>         the type parameter
     * @param <U>         the type parameter
     * @param constructor the constructor
     * @param subClass    the sub class
     * @param args        the args
     * @return the u
     * @throws InvocationTargetException the invocation target exception
     * @throws IllegalArgumentException  the illegal argument exception
     * @throws IllegalAccessException    the illegal access exception
     * @throws InstantiationException    the instantiation exception
     */
    @NonNull
    <T, U> U newInstanceSpecial(@NonNull Constructor<T> constructor, @NonNull Class<U> subClass, Object... args) throws InvocationTargetException, IllegalArgumentException, IllegalAccessException, InstantiationException;

    /**
     * Log.
     *
     * @param message the message
     */
    void log(@NonNull String message);

    /**
     * Log.
     *
     * @param message   the message
     * @param throwable the throwable
     */
    void log(@NonNull String message, @NonNull Throwable throwable);

    /**
     * Parse dex dex parser.
     *
     * @param dexData            the dex data
     * @param includeAnnotations the include annotations
     * @return the dex parser
     * @throws IOException the io exception
     */
    @Nullable
    DexParser parseDex(@NonNull ByteBuffer dexData, boolean includeAnnotations) throws IOException;


    // Methods the same with Context

    /**
     * Gets shared preferences.
     *
     * @param name the name
     * @param mode the mode
     * @return the shared preferences
     */
    SharedPreferences getSharedPreferences(String name, int mode);

    /**
     * Open file input file input stream.
     *
     * @param name the name
     * @return the file input stream
     * @throws FileNotFoundException the file not found exception
     */
    FileInputStream openFileInput(String name) throws FileNotFoundException;

    /**
     * File list string [ ].
     *
     * @return the string [ ]
     */
    String[] fileList();

    /**
     * Gets resources.
     *
     * @return the resources
     */
    Resources getResources();

    /**
     * Gets class loader.
     *
     * @return the class loader
     */
    ClassLoader getClassLoader();

    /**
     * Gets application info.
     *
     * @return the application info
     */
    ApplicationInfo getApplicationInfo();
}
