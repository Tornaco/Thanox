/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package rx2.android.schedulers;

import com.elvishew.xlog.XLog;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;

import java.util.concurrent.TimeUnit;

public final class DroppingScheduler extends Scheduler {

    @Override
    public Disposable scheduleDirect(Runnable run, long delay, TimeUnit unit) {
        XLog.e(new Throwable(), "DroppingScheduler throws %s, is system started?", run);
        return Disposables.empty();
    }

    @Override
    public Worker createWorker() {
        return new HandlerWorker();
    }

    private static final class HandlerWorker extends Worker {
        private volatile boolean disposed = false;

        @Override
        public Disposable schedule(Runnable run, long delay, TimeUnit unit) {
            XLog.e(new Throwable(), "DroppingScheduler throws %s, is system started?", run);
            return Disposables.empty();
        }

        @Override
        public void dispose() {
            disposed = true;
        }

        @Override
        public boolean isDisposed() {
            return disposed;
        }
    }
}
