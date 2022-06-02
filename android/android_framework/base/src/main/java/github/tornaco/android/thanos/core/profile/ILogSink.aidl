package github.tornaco.android.thanos.core.profile;

interface ILogSink {
    oneway void log(String message);
}