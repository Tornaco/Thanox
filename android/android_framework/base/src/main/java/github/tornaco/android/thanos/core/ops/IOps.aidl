package github.tornaco.android.thanos.core.ops;

interface IOps {
    void setMode(int code, in Pkg pkg, int mode);
    int getMode(int code, in Pkg pkg);
    String opToName(int code);
    String opToPermission(int code);
}
