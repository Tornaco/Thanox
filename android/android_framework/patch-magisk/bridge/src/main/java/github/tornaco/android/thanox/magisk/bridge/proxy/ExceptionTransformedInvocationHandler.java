package github.tornaco.android.thanox.magisk.bridge.proxy;

import android.os.RemoteException;
import android.util.Log;

import com.elvishew.xlog.XLog;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public interface ExceptionTransformedInvocationHandler {
    default Object tryInvoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        try {
            return method.invoke(proxy, args);
        } catch (InvocationTargetException ite) {
            Throwable cause = ite.getCause();
            XLog.d("ERROR- tryInvoke %s got InvocationTargetException %s, try throw getCause", method.toString(), cause);
            if (cause == null) {
                throw new RemoteException("Unknown cause.");
            } else {
                throw cause;
            }
        } catch (Throwable e) {
            XLog.d("ERROR- tryInvoke %s got Throwable %s, throw as RemoteException", method.toString(), Log.getStackTraceString(e));
            throw new RemoteException(e.getMessage());
        }
    }
}
