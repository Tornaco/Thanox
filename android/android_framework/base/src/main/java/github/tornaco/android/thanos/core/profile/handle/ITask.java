package github.tornaco.android.thanos.core.profile.handle;

import github.tornaco.android.thanos.core.pm.Pkg;

@HandlerName("task")
public
interface ITask {

    void removeTasksForPackage(String pkgName);

    void removeTasksForPackage(String pkgName, int userId);

    void removeTasksForPackage(Pkg pkg);

    boolean hasTaskFromPackage(String pkgName);

    boolean hasTaskFromPackage(String pkgName, int userId);

    boolean hasTaskFromPackage(Pkg pkg);

    void removeAllRecentTasks();

    void clearBackgroundTasks();
}
