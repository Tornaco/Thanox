package github.tornaco.android.plugin.push.message.delegate;

import java.util.concurrent.Callable;

import github.tornaco.android.plugin.push.message.delegate.server.PushDelegateManagerNative;

public class StatusCallable implements Callable<Integer> {

    @Override
    public Integer call() {
        return PushDelegateManagerNative.isServiceInstalled() ? 0 : 9;
    }
}
