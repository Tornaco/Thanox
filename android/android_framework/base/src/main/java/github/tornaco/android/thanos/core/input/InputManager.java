package github.tornaco.android.thanos.core.input;

import android.view.KeyEvent;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@AllArgsConstructor
public class InputManager {
    private final IInputManager server;

    @SneakyThrows
    public boolean injectKey(int keyCode) {
        return server.injectKey(keyCode);
    }

    @SneakyThrows
    public int getLastKey() {
        return server.getLastKey();
    }

    @SneakyThrows
    public void onKeyEvent(KeyEvent keyEvent, String source) {
        server.onKeyEvent(keyEvent, source);
    }
}
