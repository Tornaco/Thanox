package github.tornaco.permission.compiler;

import java.util.Arrays;

public class XposedHookInfo implements Comparable<XposedHookInfo> {

    private String className;
    private int[] targetSdkVersion;
    private boolean active;
    private int priority;

    public XposedHookInfo(String className, int[] targetSdkVersion, boolean active, int priority) {
        this.className = className;
        this.targetSdkVersion = targetSdkVersion;
        this.active = active;
        this.priority = priority;
    }

    public String getClassName() {
        return className;
    }

    public int[] getTargetSdkVersion() {
        return targetSdkVersion;
    }

    public boolean isActive() {
        return active;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return "XposedHookInfo{" +
                "className='" + className + '\'' +
                ", targetSdkVersion=" + Arrays.toString(targetSdkVersion) +
                ", active=" + active +
                '}';
    }

    @Override
    public int compareTo(XposedHookInfo xposedHookInfo) {
        return Integer.compare(priority, xposedHookInfo.priority);
    }
}
