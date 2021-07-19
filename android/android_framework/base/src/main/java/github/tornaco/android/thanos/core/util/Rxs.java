package github.tornaco.android.thanos.core.util;

import android.os.Handler;
import android.util.Log;

import com.elvishew.xlog.XLog;

import java.util.concurrent.Executor;

import github.tornaco.android.thanos.core.annotation.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import lombok.AllArgsConstructor;

public class Rxs {

    public static final Consumer<Throwable> ON_ERROR_LOGGING = new Consumer<Throwable>() {
        @Override
        public void accept(Throwable object) throws Exception {
            XLog.e(Log.getStackTraceString(object));
        }
    };

    public static final Action EMPTY_ACTION = new Action() {
        @Override
        public void run() throws Exception {
            // Empty.
        }
    };

    public static final Consumer<Object> EMPTY_CONSUMER = new Consumer<Object>() {
        @Override
        public void accept(Object o) throws Exception {
            // Noop.
        }
    };

    public static Executor fromHandler(Handler handler) {
        return new HandlerExecutor(handler);
    }

    @AllArgsConstructor
    private static class HandlerExecutor implements Executor {
        private Handler handler;

        @Override
        public void execute(@NonNull Runnable runnable) {
            handler.post(runnable);
        }
    }

    public static class Executors {
        private static final Executor IO = java.util.concurrent.Executors.newCachedThreadPool();

        private Executors() {
        }

        public static Executor io() {
            return IO;
        }
    }
}
