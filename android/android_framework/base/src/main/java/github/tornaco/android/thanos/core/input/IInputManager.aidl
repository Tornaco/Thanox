package github.tornaco.android.thanos.core.input;

interface IInputManager {
    boolean injectKey(int keyCode);
    int getLastKey();
    void onKeyEvent(in KeyEvent keyEvent, String source);
}