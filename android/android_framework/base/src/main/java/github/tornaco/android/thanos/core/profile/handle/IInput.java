package github.tornaco.android.thanos.core.profile.handle;

@HandlerName("input")
public
interface IInput {

    boolean injectKey(int code);

    int getLastKey();

    long getLastKeyTime();
}
