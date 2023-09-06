package io.github.libxposed.service;

import android.content.SharedPreferences;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@SuppressWarnings("unused")
public final class XposedService {

    public final static class ServiceException extends RuntimeException {
        ServiceException(String message) {
            super(message);
        }

        ServiceException(RemoteException e) {
            super("Xposed service error", e);
        }
    }

    private final static Map<OnScopeEventListener, IXposedScopeCallback> scopeCallbacks = new WeakHashMap<>();

    /**
     * Callback interface for module scope request.
     */
    public interface OnScopeEventListener {
        /**
         * Callback when the request notification / window prompted.
         *
         * @param packageName Package name of requested app
         */
        default void onScopeRequestPrompted(String packageName) {
        }

        /**
         * Callback when the request is approved.
         *
         * @param packageName Package name of requested app
         */
        default void onScopeRequestApproved(String packageName) {
        }

        /**
         * Callback when the request is denied.
         *
         * @param packageName Package name of requested app
         */
        default void onScopeRequestDenied(String packageName) {
        }

        /**
         * Callback when the request is timeout or revoked.
         *
         * @param packageName Package name of requested app
         */
        default void onScopeRequestTimeout(String packageName) {
        }

        /**
         * Callback when the request is failed.
         *
         * @param packageName Package name of requested app
         * @param message     Error message
         */
        default void onScopeRequestFailed(String packageName, String message) {
        }

        private IXposedScopeCallback asInterface() {
            return scopeCallbacks.computeIfAbsent(this, (listener) -> new IXposedScopeCallback.Stub() {
                @Override
                public void onScopeRequestPrompted(String packageName) {
                    listener.onScopeRequestPrompted(packageName);
                }

                @Override
                public void onScopeRequestApproved(String packageName) {
                    listener.onScopeRequestApproved(packageName);
                }

                @Override
                public void onScopeRequestDenied(String packageName) {
                    listener.onScopeRequestDenied(packageName);
                }

                @Override
                public void onScopeRequestTimeout(String packageName) {
                    listener.onScopeRequestTimeout(packageName);
                }

                @Override
                public void onScopeRequestFailed(String packageName, String message) {
                    listener.onScopeRequestFailed(packageName, message);
                }
            });
        }
    }

    public enum Privilege {
        /**
         * Unknown privilege value.
         */
        FRAMEWORK_PRIVILEGE_UNKNOWN,

        /**
         * The framework is running as root.
         */
        FRAMEWORK_PRIVILEGE_ROOT,

        /**
         * The framework is running in a container with a fake system_server.
         */
        FRAMEWORK_PRIVILEGE_CONTAINER,

        /**
         * The framework is running as a different app, which may have at most shell permission.
         */
        FRAMEWORK_PRIVILEGE_APP,

        /**
         * The framework is embedded in the hooked app, which means {@link #getRemotePreferences} will be null and remote file is unsupported.
         */
        FRAMEWORK_PRIVILEGE_EMBEDDED
    }

    private final IXposedService mService;
    private final Map<String, RemotePreferences> mRemotePrefs = new HashMap<>();

    final ReentrantReadWriteLock deletionLock = new ReentrantReadWriteLock();

    XposedService(IXposedService service) {
        mService = service;
    }

    IXposedService getRaw() {
        return mService;
    }

    /**
     * Get the Xposed API version of current implementation.
     *
     * @return API version
     * @throws ServiceException If the service is dead or an error occurred
     */
    public int getAPIVersion() {
        try {
            return mService.getAPIVersion();
        } catch (RemoteException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Get the Xposed framework name of current implementation.
     *
     * @return Framework name
     * @throws ServiceException If the service is dead or an error occurred
     */
    @NonNull
    public String getFrameworkName() {
        try {
            return mService.getFrameworkName();
        } catch (RemoteException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Get the Xposed framework version of current implementation.
     *
     * @return Framework version
     * @throws ServiceException If the service is dead or an error occurred
     */
    @NonNull
    public String getFrameworkVersion() {
        try {
            return mService.getFrameworkVersion();
        } catch (RemoteException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Get the Xposed framework version code of current implementation.
     *
     * @return Framework version code
     * @throws ServiceException If the service is dead or an error occurred
     */
    public long getFrameworkVersionCode() {
        try {
            return mService.getFrameworkVersionCode();
        } catch (RemoteException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Get the Xposed framework privilege of current implementation.
     *
     * @return Framework privilege
     * @throws ServiceException If the service is dead or an error occurred
     */
    @NonNull
    public Privilege getFrameworkPrivilege() {
        try {
            int value = mService.getFrameworkPrivilege();
            return (value >= 0 && value <= 3) ? Privilege.values()[value + 1] : Privilege.FRAMEWORK_PRIVILEGE_UNKNOWN;
        } catch (RemoteException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Get the application scope of current module.
     *
     * @return Module scope
     * @throws ServiceException If the service is dead or an error occurred
     */
    @NonNull
    public List<String> getScope() {
        try {
            return mService.getScope();
        } catch (RemoteException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Request to add a new app to the module scope.
     *
     * @param packageName Package name of the app to be added
     * @param callback    Callback to be invoked when the request is completed or error occurred
     * @throws ServiceException If the service is dead or an error occurred
     */
    public void requestScope(@NonNull String packageName, @NonNull OnScopeEventListener callback) {
        try {
            mService.requestScope(packageName, callback.asInterface());
        } catch (RemoteException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Remove an app from the module scope.
     *
     * @param packageName Package name of the app to be added
     * @return null if successful, or non-null with error message
     * @throws ServiceException If the service is dead or an error occurred
     */
    @Nullable
    public String removeScope(@NonNull String packageName) {
        try {
            return mService.removeScope(packageName);
        } catch (RemoteException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Get remote preferences from Xposed framework. If the group does not exist, it will be created.
     *
     * @param group Group name
     * @return The preferences
     * @throws ServiceException              If the service is dead or an error occurred
     * @throws UnsupportedOperationException If the framework is embedded
     */
    @NonNull
    public SharedPreferences getRemotePreferences(@NonNull String group) {
        return mRemotePrefs.computeIfAbsent(group, k -> {
            try {
                var instance = RemotePreferences.newInstance(this, k);
                if (instance == null) {
                    throw new ServiceException("Framework returns null");
                }
                return instance;
            } catch (RemoteException e) {
                throw new ServiceException(e);
            }
        });
    }

    /**
     * Delete a group of remote preferences.
     *
     * @param group Group name
     * @throws ServiceException              If the service is dead or an error occurred
     * @throws UnsupportedOperationException If the framework is embedded
     */
    public void deleteRemotePreferences(@NonNull String group) {
        deletionLock.writeLock().lock();
        try {
            mService.deleteRemotePreferences(group);
            mRemotePrefs.computeIfPresent(group, (k, v) -> {
                v.setDeleted();
                return null;
            });
        } catch (RemoteException e) {
            throw new ServiceException(e);
        } finally {
            deletionLock.writeLock().unlock();
        }
    }

    /**
     * List all files in the module's shared data directory.
     *
     * @return The file list
     * @throws ServiceException              If the service is dead or an error occurred
     * @throws UnsupportedOperationException If the framework is embedded
     */
    @NonNull
    public String[] listRemoteFiles() {
        try {
            var files = mService.listRemoteFiles();
            if (files == null) throw new ServiceException("Framework returns null");
            return files;
        } catch (RemoteException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Open a file in the module's shared data directory. The file will be created if not exists.
     *
     * @param name File name, must not contain path separators and . or ..
     * @return The file descriptor
     * @throws ServiceException              If the service is dead or an error occurred
     * @throws UnsupportedOperationException If the framework is embedded
     */
    @NonNull
    public ParcelFileDescriptor openRemoteFile(@NonNull String name) {
        try {
            var file = mService.openRemoteFile(name);
            if (file == null) throw new ServiceException("Framework returns null");
            return file;
        } catch (RemoteException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Delete a file in the module's shared data directory.
     *
     * @param name File name, must not contain path separators and . or ..
     * @return true if successful, false if the file does not exist
     * @throws ServiceException              If the service is dead or an error occurred
     * @throws UnsupportedOperationException If the framework is embedded
     */
    public boolean deleteRemoteFile(@NonNull String name) {
        try {
            return mService.deleteRemoteFile(name);
        } catch (RemoteException e) {
            throw new ServiceException(e);
        }
    }
}
