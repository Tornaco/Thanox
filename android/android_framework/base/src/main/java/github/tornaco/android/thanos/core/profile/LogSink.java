package github.tornaco.android.thanos.core.profile;

import android.os.Handler;
import android.os.Looper;

public class LogSink {

    protected void log(String message) {

    }

    public final ILogSink.Stub stub = new ILogSink.Stub() {
        private final Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public void log(String message) {
            handler.post(() -> LogSink.this.log(message));
        }
    };
}
