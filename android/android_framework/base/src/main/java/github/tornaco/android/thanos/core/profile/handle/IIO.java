package github.tornaco.android.thanos.core.profile.handle;

@HandlerName("io")
public
interface IIO {

    String read(String path);

    boolean write(String path, String content);

    boolean writeAppend(String path, String content);
}
