package github.tornaco.android.thanos.core.push;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@AllArgsConstructor
public class PushManager {

    private final IPushManager server;

    @SneakyThrows
    public void registerChannel(PushChannel channel) {
        server.registerChannel(channel);
    }

    @SneakyThrows
    public void unRegisterChannel(PushChannel channel) {
        server.unRegisterChannel(channel);
    }

}
