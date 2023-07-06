package github.tornaco.android.thanos.core.ops;

interface IOps {
    void setMode(int code, in Pkg pkg, int mode);
    String getPermState(int code, in Pkg pkg);

    String opToName(int code);
    String opToPermission(int code);

    int getPermissionFlags(String permName, in Pkg pkg);
    String permissionFlagToString(int flag);
}
