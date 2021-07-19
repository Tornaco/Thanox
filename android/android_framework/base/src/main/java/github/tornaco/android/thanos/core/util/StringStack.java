package github.tornaco.android.thanos.core.util;

import java.util.Stack;

public final class StringStack extends Stack<String> {
    static final long serialVersionUID = -1506910875640317898L;

    public StringStack() {
    }

    public String peekString() {
        return super.peek();
    }

    public String popString() {
        return super.pop();
    }

    public String pushString(String val) {
        return super.push(val);
    }
}
