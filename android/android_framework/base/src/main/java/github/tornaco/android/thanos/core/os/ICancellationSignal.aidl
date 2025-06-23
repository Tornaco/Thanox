package github.tornaco.android.thanos.core.os;

interface ICancellationSignal {
    oneway void cancel();
}